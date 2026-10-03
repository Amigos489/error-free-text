package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.services.TextCorrectionService;
import balabol.error.free.text.services.YandexSpellerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

    private final int IS_DIGITS = 2;

    private final int IS_URL = 4;

    private final YandexSpellerService yandexSpellerService;

    @Override
    public String correctText(String uncorrectedText) {

        LOGGER.info("method correctText has started working");

        LOGGER.info("текст для исправления: {}", uncorrectedText);

        List<String> textList = Collections.singletonList(uncorrectedText);

        StringBuilder correctedTextBuilder = new StringBuilder(uncorrectedText);

        LOGGER.info("текст для исправления: {}", uncorrectedText);

        List<List<SpellResult>> resultCorrected = yandexSpellerService.sendTextResult(textList, 0);

        for (List<SpellResult> listSpellResult : resultCorrected) {
            for (SpellResult spellResult : listSpellResult) {
                if (spellResult.s().isEmpty()) {
                    LOGGER.warn("нечего заменять тут");
                    continue;
                }
                LOGGER.info("spellResult.pos() = {}, spellResult.pos() + spellResult.len() = {}",
                        spellResult.pos(), spellResult.pos() + spellResult.len());
                LOGGER.info(correctedTextBuilder.substring(spellResult.pos(), spellResult.pos() + spellResult.len()));
                LOGGER.info(spellResult.s().get(0));

                correctedTextBuilder.replace(spellResult.pos(),
                        (spellResult.pos() + spellResult.len()),
                        spellResult.s().get(0));

                LOGGER.info("промежуточный исправленный текст: {}",
                        correctedTextBuilder.toString());
            }
        }

        LOGGER.info("исправленный текст: {}", correctedTextBuilder.toString());

        LOGGER.info("method correctText has finished work");

        return correctedTextBuilder.toString();
    }

    @Override
    public boolean ifRegexAreInTheText(String text, String regex) {

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }
}
