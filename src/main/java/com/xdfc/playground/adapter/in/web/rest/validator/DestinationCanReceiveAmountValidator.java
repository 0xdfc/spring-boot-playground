package com.xdfc.playground.adapter.in.web.rest.validator;

import com.xdfc.playground.adapter.in.web.rest.annotation.DestinationCanReceiveAmount;
import com.xdfc.playground.adapter.in.web.rest.constant.AccountConstants;
import com.xdfc.playground.domain.dto.InternalAccountTransferDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DestinationCanReceiveAmountValidator implements ConstraintValidator<
    DestinationCanReceiveAmount,
    InternalAccountTransferDto
> {
    @Override
    public boolean isValid(
        InternalAccountTransferDto internalAccountTransferDto,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        return internalAccountTransferDto.destination().getBalance()
            .add(internalAccountTransferDto.amount())
            .compareTo(AccountConstants.MaximumAccountBalance) < 0;
    }
}
