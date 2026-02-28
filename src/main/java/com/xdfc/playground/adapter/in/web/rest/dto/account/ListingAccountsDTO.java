package com.xdfc.playground.adapter.in.web.rest.dto.account;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ListingAccountsDTO(
    @NotNull
    Set<ListingAccountDTO> accounts
) {}
