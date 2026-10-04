package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.services.TextCorrectionService;
import balabol.error.free.text.services.YandexSpellerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class TextCorrectionServiceImpl implements TextCorrectionService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(TextCorrectionServiceImpl.class.getName());

    private final String regexForCheckUrlsInText
            = "((http://|https://)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(/([a-zA-Z-_/.0-9#:?=&;,]*)?)?)";

    private final String regexForCheckNumbersInText
            = "[0-9]+";

    private final int IGNORE_DIGITS = 2;

    private final int IGNORE_URLS = 4;

    private final YandexSpellerService yandexSpellerService;

    @Override
    public boolean ifRegexAreInTheText(String text, String regex) {

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    @Override
    public String correctErrorsInText(String uncorrectedText) {

        StringBuilder correctedTextBuilder
                = new StringBuilder(uncorrectedText);

        int options = 0;

        if (ifRegexAreInTheText(uncorrectedText,
                regexForCheckNumbersInText)) {
            options += IGNORE_DIGITS;
        }

        if (ifRegexAreInTheText(uncorrectedText,
                regexForCheckUrlsInText)) {
            options += IGNORE_URLS;
        }

        TextRequest textRequest = new TextRequest(uncorrectedText, options);

        List<List<SpellResult>> spellCheckResults = sendRequestOnCheckSpelling(textRequest);

        // Todo: вынести в отдельный метод обработку полученного запроса (билдер, лист со спелл резалт)
        for (List<SpellResult> listSpellResult : spellCheckResults) {
            for (SpellResult spellResult : listSpellResult) {

                if (spellResult.s().isEmpty()) {
                    continue;
                }

                correctedTextBuilder.replace(spellResult.pos(),
                        (spellResult.pos() + spellResult.len()),
                        spellResult.s().get(0));
            }
        }

        return correctedTextBuilder.toString();
    }

    @Override
    public List<List<SpellResult>> sendRequestOnCheckSpelling(TextRequest textRequest) {
        return yandexSpellerService.sendTextRequest(textRequest);
    }
}
