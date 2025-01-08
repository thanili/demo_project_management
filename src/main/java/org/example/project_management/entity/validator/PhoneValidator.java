package org.example.project_management.entity.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String PATTERN = "^(\\+|00)?[1-9][0-9]{1,3}?\\s?[0-9]{6,14}(\\s?[0-9]{2,6})?$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.matches(PATTERN);
    }
}
