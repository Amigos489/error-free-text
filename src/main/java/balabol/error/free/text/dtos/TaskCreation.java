package balabol.error.free.text.dtos;

import balabol.error.free.text.enums.LanguageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreation(
        @NotBlank(message = "text cannot be empty")
        @Size(message = "text cannot be less 3 char", min = 3)
        String text,

        //Todo: валидация на все случаи
        @NotNull(message = "language cannot be empty")
        LanguageType language
) {}
