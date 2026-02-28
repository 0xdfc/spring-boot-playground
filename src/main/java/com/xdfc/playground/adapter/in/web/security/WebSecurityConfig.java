package com.xdfc.playground.adapter.in.web.security;

import com.xdfc.playground.adapter.in.web.security.filter.JwtSecurityFilter;
import com.xdfc.playground.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtSecurityFilter jwtSecurityFilter;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(
                authorize -> {
                    authorize.requestMatchers("/auth/manage/**")
                            .permitAll();
                    authorize.requestMatchers("/error").permitAll();

                    authorize.anyRequest().authenticated();
                }
            )
        ;

        // TODO ->> might not work due to disabling httpBasic and formLogin?
        http.addFilterBefore(this.jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> this.userService.findByUsername(username).orElseThrow(
            // Generic message to avoid disclosure of issue and in turn make it
            // harder to brute given lack of information on existence of user.
            () -> new UsernameNotFoundException("validation.check.and.try.again")
        );
    }

    // ? I could add a bean here changing the default password encoder
    // ? but the docs seem to suggest the defaults that are used are what
    // ? is currently considered best practice
}
