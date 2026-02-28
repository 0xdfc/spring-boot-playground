package com.xdfc.playground.adapter.in.web.rest.validator;

import com.xdfc.playground.adapter.in.web.rest.annotation.IsEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// https://www.baeldung.com/javax-validations-enums#bd-any-of-validation
final public class IsEnumValidator implements ConstraintValidator<IsEnum, CharSequence> {
    private Set<String> acceptableEnumValues;

    @Override
    public void initialize(IsEnum constraintAnnotation) {
        this.acceptableEnumValues  = Stream.of(
            constraintAnnotation.enumerable().getEnumConstants()
        )
            .map(Enum::name)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(
        @NotNull final CharSequence charSequence,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        return this.acceptableEnumValues.contains(charSequence.toString());
    }
}
