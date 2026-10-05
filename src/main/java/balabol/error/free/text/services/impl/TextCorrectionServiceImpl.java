package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.services.TextCorrectionService;
import balabol.error.free.text.services.YandexSpellerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private final int MAX_TEXT_LENGTH = 10_000;

    private final YandexSpellerService yandexSpellerService;

    @Override
    public String correctErrorsInText(String uncorrectedText) {

        LOGGER.info("method correctErrorsInText has started working with parameters: {}",
                uncorrectedText);

        int options = 0;

        if (ifRegexAreInTheText(uncorrectedText,
                regexForCheckNumbersInText)) {
            options += IGNORE_DIGITS;
        }

        if (ifRegexAreInTheText(uncorrectedText,
                regexForCheckUrlsInText)) {
            options += IGNORE_URLS;
        }

        // Todo: Логика если текст больше 10000 символов

        if (uncorrectedText.length() > MAX_TEXT_LENGTH) {

            StringBuilder correctedTextBuilder
                    = new StringBuilder(uncorrectedText);

            int globalPosition = 0;

            int index = 0;

            List<String> text = new ArrayList<>();
            while (index < uncorrectedText.length()) {
                text.add(uncorrectedText.substring(index,
                        Math.min(index + MAX_TEXT_LENGTH, uncorrectedText.length())));
                index += MAX_TEXT_LENGTH;
            }

            LOGGER.info("number of parts = {}", text.size());

            for (String partText : text) {

                // Todo: как лучше поэтапно отправлять
                //  и менять или сразу всё отправить
                //  и потом обрабатывать по списку (спросить или замерить)

                TextRequest textRequest = new TextRequest(partText, options);

                List<List<SpellResult>> spellCheckResults = sendRequestOnCheckSpelling(textRequest);

                correctedTextBuilder = processResponseFromSpellingCheck(correctedTextBuilder, spellCheckResults);

                globalPosition += MAX_TEXT_LENGTH;
            }

            return correctedTextBuilder.toString();
        }

        TextRequest textRequest = new TextRequest(uncorrectedText, options);

        List<List<SpellResult>> spellCheckResults = sendRequestOnCheckSpelling(textRequest);


        String correctedText = processResponseFromSpellingCheck(
                uncorrectedText,
                spellCheckResults
        );

        LOGGER.info("method correctErrorsInText has finished work, corrected text = {}",
                correctedText);

        return correctedText;
    }

    @Override
    public boolean ifRegexAreInTheText(String text, String regex) {

        LOGGER.info("method ifRegexAreInTheText has started working with parameters: text = {}, regex = {}",
                text, regex);

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);

        LOGGER.info("method ifRegexAreInTheText has finished work");

        return matcher.find();
    }

    @Override
    public List<List<SpellResult>> sendRequestOnCheckSpelling(TextRequest textRequest) {

        LOGGER.info("method sendRequestOnCheckSpelling has started working with parameters: " +
                        "textRequest = {}", textRequest);

        List<List<SpellResult>> spellCheckResults = yandexSpellerService.sendTextRequest(textRequest);

        LOGGER.info("method sendRequestOnCheckSpelling has finished work");

        return spellCheckResults;
    }

    @Override
    public String processResponseFromSpellingCheck(String uncorrectedText,
                                                   List<List<SpellResult>> spellCheckResults) {

        LOGGER.info("method processResponseFromSpellingCheck has started working");

        StringBuilder correctedTextBuilder
                = new StringBuilder(uncorrectedText);

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

        LOGGER.info("method processResponseFromSpellingCheck has finished work");

        return correctedTextBuilder.toString();
    }

    public StringBuilder processResponseFromSpellingCheck(StringBuilder textBuilder,
                                                          List<List<SpellResult>> spellCheckResults) {

        LOGGER.info("method _ has started working");

        int bias = 0;

        for (List<SpellResult> listSpellResult : spellCheckResults) {
            for (SpellResult spellResult : listSpellResult) {

                if (spellResult.s().isEmpty()) {
                    continue;
                }

                //Берём первое слово из списка
                String wordForReplace = spellResult.s().get(0);

                int startPosition = spellResult.pos() + bias;

                int endPosition = startPosition + spellResult.len();

                LOGGER.info("startPosition = {}, endPosition = {}, bias = {}, wordForReplace = {}",
                        startPosition, endPosition, bias, wordForReplace);

                LOGGER.info(textBuilder.substring(startPosition, endPosition));
                textBuilder.replace(startPosition, endPosition, wordForReplace);

                LOGGER.info(textBuilder.toString());

                if (wordForReplace.length() > spellResult.word().length()) {
                    bias += wordForReplace.length() - spellResult.word().length();
                }

                if (wordForReplace.length() < spellResult.word().length()) {
                    bias -= spellResult.word().length() - wordForReplace.length();
                }
            }
        }

        LOGGER.info("method _ has finished work");

        return textBuilder;
    }

    @Override
    public String correctErrorsInLargeText(String uncorrectedText) {

        int options = 0;

        if (ifRegexAreInTheText(uncorrectedText,
                regexForCheckNumbersInText)) {
            options += IGNORE_DIGITS;
        }

        if (ifRegexAreInTheText(uncorrectedText,
                regexForCheckUrlsInText)) {
            options += IGNORE_URLS;
        }

        StringBuilder uncorrectedTextBuilder;

        if (uncorrectedText.length() > MAX_TEXT_LENGTH) {

            uncorrectedTextBuilder = new StringBuilder();

            //Todo: отдельный метод
            int index = 0;

            List<String> textFromParts = new ArrayList<>();
            while (index < uncorrectedText.length()) {
                textFromParts.add(uncorrectedText.substring(index,
                        Math.min(index + MAX_TEXT_LENGTH, uncorrectedText.length())));
                index += MAX_TEXT_LENGTH;
            }

            for (String textPart : textFromParts) {

                StringBuilder textPartBuilder = new StringBuilder(textPart);

                TextRequest textRequest = new TextRequest(textPart, options);

                List<List<SpellResult>> spellCheckResults = sendRequestOnCheckSpelling(textRequest);

                textPartBuilder = processResponseFromSpellingCheck(
                        textPartBuilder,
                        spellCheckResults
                );

                uncorrectedTextBuilder.append(textPartBuilder);
            }

        } else {
            uncorrectedTextBuilder = new StringBuilder(uncorrectedText);

            TextRequest textRequest = new TextRequest(uncorrectedText, options);

            List<List<SpellResult>> spellCheckResults = sendRequestOnCheckSpelling(textRequest);

            uncorrectedTextBuilder = processResponseFromSpellingCheck(
                    uncorrectedTextBuilder,
                    spellCheckResults
            );
        }

        return uncorrectedTextBuilder.toString();
    }
}
