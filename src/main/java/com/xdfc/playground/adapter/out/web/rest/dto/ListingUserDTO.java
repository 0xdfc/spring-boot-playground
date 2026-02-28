package com.xdfc.playground.adapter.out.web.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record ListingUserDTO(
    @NotEmpty
    String id,

    @NotEmpty
    @Email
    String username
) { }
