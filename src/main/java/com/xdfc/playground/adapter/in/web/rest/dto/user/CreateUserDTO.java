package com.xdfc.playground.adapter.in.web.rest.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank
        @Email
        String username,

        @NotBlank
        @Size(min = 8, max = 64)
        // TODO ->> add regex password pattern or make
        // TODO ^->> a meta annotation?
        // TODO ->> add password confirmation field?
        // @Pattern()
        String password

        // TODO ->> add a confirmed_password?
) {
    public static CreateUserDTO make(String username, String password) {
        return new CreateUserDTO(username, password);
    }
}
