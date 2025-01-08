package org.example.project_management.entity.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoubleRangeValidator implements ConstraintValidator<DoubleRange, Double> {
    private double min;
    private double max;

    @Override
    public void initialize(DoubleRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Consider null valid; use @NotNull for null checks
        }
        return value >= min && value <= max;
    }
}
