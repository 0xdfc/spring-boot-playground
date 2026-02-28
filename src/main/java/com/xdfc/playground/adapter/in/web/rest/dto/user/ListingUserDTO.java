package com.xdfc.playground.adapter.in.web.rest.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record ListingUserDTO(
    @NotEmpty
    String id,

    @NotEmpty
    @Email
    String username
) { }
