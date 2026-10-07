package balabol.error.free.text.annotations;

import balabol.error.free.text.validators.LanguageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = LanguageValidator.class)
public @interface Language {
    String message() default "language must be one of following values: [RU, EN]";
    Class[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
