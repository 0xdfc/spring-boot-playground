package com.xdfc.playground.adapter.in.web.rest.dto.message;

import jakarta.validation.constraints.NotBlank;

public record ErrorMessage(@NotBlank String message) { }
