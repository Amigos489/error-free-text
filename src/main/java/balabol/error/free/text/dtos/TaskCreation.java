package balabol.error.free.text.dtos;

import balabol.error.free.text.annotations.Language;
import balabol.error.free.text.annotations.NoAllNumbersOrSpecSymbols;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreation(
        @NoAllNumbersOrSpecSymbols
        @NotBlank(message = "text cannot be empty")
        @Size(message = "text cannot be less 3 char", min = 3)
        String text,

        @Language
        @NotNull(message = "language cannot be empty")
        String language
) {}
