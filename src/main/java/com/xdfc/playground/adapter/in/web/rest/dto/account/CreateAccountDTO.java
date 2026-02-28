package com.xdfc.playground.adapter.in.web.rest.dto.account;

import com.xdfc.playground.adapter.in.web.rest.annotation.IsEnum;
import com.xdfc.playground.domain.enumerable.CurrencyCode;

public record CreateAccountDTO(
    @IsEnum(enumerable = CurrencyCode.class)
    String code
) {}
