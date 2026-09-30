package balabol.error.free.text.dtos;

import balabol.error.free.text.enums.LanguageType;

public record TaskCreation(
        String text,
        LanguageType type
) {}
