package com.xdfc.playground.adapter.in.web.rest.dto.account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.money.MonetaryAmount;

public record ListingAccountDTO(
    @NotEmpty
    String id,

    @NotEmpty
    String address
) { }
