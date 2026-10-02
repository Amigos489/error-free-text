package balabol.error.free.text.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoAllNumbersOrSpecSymbolsValidator
        implements ConstraintValidator<balabol.error.free.text.annotations.NoAllNumbersOrSpecSymbols, String> {

    @Override
    public void initialize(balabol.error.free.text.annotations.NoAllNumbersOrSpecSymbols constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string,
                           ConstraintValidatorContext constraintValidatorContext) {
        return !(string.matches("[^a-zA-Zа-яА-Я]+"));
    }
}
