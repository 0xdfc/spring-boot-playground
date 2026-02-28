package com.xdfc.playground.adapter.in.web.rest.validator;

import com.xdfc.playground.adapter.in.web.rest.annotation.UniqueAccountTransferAddresses;
import com.xdfc.playground.adapter.in.web.rest.dto.account.AccountTransferDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// This validator could be made more generic and useful for other cases,
// but I need to learn more about Java for that (next steps).
public class UniqueAccountTransferAddressesValidator implements ConstraintValidator<
    UniqueAccountTransferAddresses,
    AccountTransferDto
> {
    @Override
    public boolean isValid(
        final AccountTransferDto accountTransferDto,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        return !accountTransferDto.to().equals(accountTransferDto.from());
    }
}
