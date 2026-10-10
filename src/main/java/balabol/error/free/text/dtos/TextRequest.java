package balabol.error.free.text.unit.dtos;

import balabol.error.free.text.unit.enums.LanguageType;

public record TextRequest(
        String text,
        int options,
        LanguageType language
) {}
