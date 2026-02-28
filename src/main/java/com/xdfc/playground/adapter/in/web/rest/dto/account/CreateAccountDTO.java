package com.xdfc.playground.adapter.in.web.rest.dto.account;

import com.xdfc.playground.domain.enumerable.CurrencyCode;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountDTO(
    @NotBlank
    CurrencyCode code
) {}
