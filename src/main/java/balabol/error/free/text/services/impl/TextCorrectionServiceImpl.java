package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.services.TextCorrectionService;
import balabol.error.free.text.services.YandexSpellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TextCorrectionServiceImpl implements TextCorrectionService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(TextCorrectionServiceImpl.class.getName());

    private int IS_DIGITS = 2;

    private int IS_URL = 4;

    private final YandexSpellerService yandexSpellerService;

    public TextCorrectionServiceImpl(YandexSpellerService yandexSpellerService) {
        this.yandexSpellerService = yandexSpellerService;
    }

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
    public boolean ifNumbersAreInTheText(String text) {
        return false;
    }

    @Override
    public boolean ifUrlsAreInTheText(String text) {
        return false;
    }
}
