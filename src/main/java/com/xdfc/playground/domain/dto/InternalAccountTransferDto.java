package com.xdfc.playground.domain.dto;

import com.xdfc.playground.adapter.in.web.rest.annotation.DestinationCanReceiveAmount;
import com.xdfc.playground.adapter.in.web.rest.annotation.SameCurrencyTransfer;
import com.xdfc.playground.adapter.in.web.rest.annotation.SufficientBalanceInSource;
import com.xdfc.playground.adapter.in.web.rest.annotation.TransferAmountLimits;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@SameCurrencyTransfer
@SufficientBalanceInSource
@DestinationCanReceiveAmount
public record InternalAccountTransferDto(
    @NotNull AccountEntity source,
    @NotNull AccountEntity destination,

    @TransferAmountLimits
    BigDecimal amount
) {}
