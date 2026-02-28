package com.xdfc.playground.adapter.in.web.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Currently and temporarily a direct copy of the CreateUserDto,
// which in time, should have additional validation rules.
public record LoginDto(
        @NotBlank
        @Email
        String username,

        @NotBlank
        @Size(min = 8, max = 64)
        String password
) { }
