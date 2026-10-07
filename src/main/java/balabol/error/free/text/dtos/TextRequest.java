package balabol.error.free.text.dtos;

import balabol.error.free.text.enums.LanguageType;

public record TextRequest(
        String text,
        int options,
        LanguageType language
) {}
