package balabol.error.free.text.annotations;

import balabol.error.free.text.validators.NoAllNumbersOrSpecSymbolsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NoAllNumbersOrSpecSymbolsValidator.class)
public @interface NoAllNumbersOrSpecSymbols {
    String message() default "text cannot contain only spec symbols and numbers";
    Class[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
