package com.xdfc.playground.adapter.in.web.rest.validator;

import com.xdfc.playground.adapter.in.web.rest.annotation.SameCurrencyTransfer;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.domain.dto.InternalAccountTransferDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

final public class SameCurrencyTransferValidator implements ConstraintValidator<
    SameCurrencyTransfer,
    InternalAccountTransferDto
> {
    @Override
    public boolean isValid(
        InternalAccountTransferDto internalAccountTransferDto,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        final AccountEntity
            accountA = internalAccountTransferDto.source(),
            accountB = internalAccountTransferDto.destination();

        return accountA.getCurrencyId().equals(accountB.getCurrencyId());
    }
}
