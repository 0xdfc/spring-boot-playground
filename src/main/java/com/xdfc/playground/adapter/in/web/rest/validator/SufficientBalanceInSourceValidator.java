package com.xdfc.playground.adapter.in.web.rest.validator;

import com.xdfc.playground.adapter.in.web.rest.annotation.SufficientBalanceInSource;
import com.xdfc.playground.domain.dto.InternalAccountTransferDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class SufficientBalanceInSourceValidator implements ConstraintValidator<
    SufficientBalanceInSource,
    InternalAccountTransferDto
> {

    @Override
    public boolean isValid(
        InternalAccountTransferDto internalAccountTransferDto,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        return internalAccountTransferDto.source()
            .getBalance()
            .subtract(internalAccountTransferDto.amount())
            .compareTo(BigDecimal.valueOf(0L)) >= 0;
    }
}
