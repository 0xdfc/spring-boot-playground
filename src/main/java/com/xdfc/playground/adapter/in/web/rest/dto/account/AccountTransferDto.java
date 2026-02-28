package com.xdfc.playground.adapter.in.web.rest.dto.account;

import com.xdfc.playground.adapter.in.web.rest.annotation.TransferAmountLimits;
import com.xdfc.playground.adapter.in.web.rest.annotation.UniqueAccountTransferAddresses;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;


@UniqueAccountTransferAddresses()
public record AccountTransferDto(
    @UUID String from,
    @UUID String to,

    @TransferAmountLimits
    BigDecimal amount
) {}
