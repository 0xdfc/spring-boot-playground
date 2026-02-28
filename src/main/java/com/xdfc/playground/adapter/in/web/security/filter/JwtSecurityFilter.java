package com.xdfc.playground.adapter.in.web.security.filter;

import com.xdfc.playground.adapter.in.web.security.jwt.JwtAuthContextHandler;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenExtractor;
import com.xdfc.playground.adapter.in.web.security.jwt.JwtTokenManager;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private JwtAuthContextHandler jwtAuthContextHandler;

    @Autowired
    private JwtTokenExtractor jwtTokenExtractor;

    @Autowired
    private UserService userService;

    private void handleRequestTokenAuth(
        final HttpServletRequest request,
        final String token
    ) {
        this.jwtTokenManager.validateToken(token);

        final Optional<UserEntity> user = this.userService.findByUsername(
            this.jwtTokenManager.tokenToUsername(token)
        );

        user.ifPresent(
            userEntity -> this.jwtAuthContextHandler
                .authorizeUserWithinSpringSecurityContext(
                    request,
                    userEntity
                )
        );

    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final Optional<String> token = this.jwtTokenExtractor.toToken(
            request
        );

        if (token.isPresent() && !token.get().isBlank())
            this.handleRequestTokenAuth(request, token.get());

        filterChain.doFilter(request, response);
    }
}
