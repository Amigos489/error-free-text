package balabol.error.free.text.unit.validators;

import balabol.error.free.text.unit.annotations.Language;
import balabol.error.free.text.unit.enums.LanguageType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LanguageValidator
        implements ConstraintValidator<Language, String> {

    @Override
    public void initialize(Language constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        try {
            if (string == null) return false;

            LanguageType.valueOf(string);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
