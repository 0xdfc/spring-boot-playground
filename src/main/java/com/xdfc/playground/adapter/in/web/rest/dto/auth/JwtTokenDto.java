package com.xdfc.playground.adapter.in.web.rest.dto.auth;

import jakarta.validation.constraints.NotEmpty;

public record JwtTokenDto(
    @NotEmpty
    String token
) { }
