package balabol.error.free.text.unit.validators;

import balabol.error.free.text.unit.annotations.NoAllNumbersOrSpecSymbols;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoAllNumbersOrSpecSymbolsValidator
        implements ConstraintValidator<NoAllNumbersOrSpecSymbols, String> {

    @Override
    public void initialize(NoAllNumbersOrSpecSymbols constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (string == null) return false;

        return !(string.matches("[^a-zA-Zа-яА-Я]+"));
    }
}
