package com.xdfc.playground.adapter.in.web.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record JwtTokenDto(
    @NotEmpty
    String token
) { }
