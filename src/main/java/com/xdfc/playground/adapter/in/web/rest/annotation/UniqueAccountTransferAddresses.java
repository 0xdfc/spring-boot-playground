package com.xdfc.playground.adapter.in.web.rest.annotation;

import com.xdfc.playground.adapter.in.web.rest.validator.UniqueAccountTransferAddressesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueAccountTransferAddressesValidator.class)
public @interface UniqueAccountTransferAddresses {
    String message() default "validation.transfer.same.account";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
