package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

// However much I'd like to use fluent accessors, here, we
// are under the requirements of the user details interface.
@Accessors(chain = true)
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

    private Collection<? extends GrantedAuthority> authorities = List.of();

    public UserEntity(
        @NotEmpty final String username,
        @NotEmpty final String password
    ) {
        this.setPassword(password)
            .setUsername(username);
    }
}
