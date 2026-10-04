package balabol.error.free.text.services;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;

import java.util.List;

public interface TextCorrectionService {
    boolean ifRegexAreInTheText(String text, String regex);

    String correctErrorsInText(String uncorrectedText);

    List<List<SpellResult>> sendRequestOnCheckSpelling(TextRequest textRequest);
}
