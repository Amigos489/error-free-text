package balabol.error.free.text.unit.services;

import balabol.error.free.text.unit.dtos.SpellResult;
import balabol.error.free.text.unit.dtos.TextRequest;
import balabol.error.free.text.unit.enums.LanguageType;

import java.util.List;

public interface TextCorrectionService {

    String correctSpellInText(String uncorrectedText,
                              LanguageType language);

    String processText(TextRequest textRequest, StringBuilder textBuilder);

    boolean ifRegexAreInTheText(String text, String regex);

    List<List<SpellResult>> sendRequestOnCheckSpellText(TextRequest textRequest);

    StringBuilder processResponseFromTextSpellCheck(List<List<SpellResult>> spellCheckResults,
                                                    StringBuilder textBuilder);

    int changeOffset(String originalWord, String wordForReplace, int offset);

    List<String> splitTextOnPart(String text, int maxLengthPart);

    int getOptionsForText(String text);
}
