package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
final public class UserEntity extends ExtendableUuidSuperEntity implements UserDetails {
    @Column(length = 320, nullable = false, unique = true)
    @Email()
    @NotBlank
    private String username;

    // TODO ->> check hash strategy outcome lengths and constrict
    // TODO ^->> the column
    @Column(nullable = false)
    @NotBlank
    private String password;

    @OneToMany()
    private Set<AccountEntity> accounts = new HashSet<>();

    public UserEntity(String username, String password) {
        this.setPassword(password);
        this.setUsername(username);
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
