package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.enums.LanguageType;
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
    public String correctSpellInText(String uncorrectedText,
                                     LanguageType language) {

        LOGGER.info("method correctSpellInText has started working with parameters: " +
                "text with length = {}, language = {}",
                uncorrectedText.length(), language);

        StringBuilder correctedTextBuilder = new StringBuilder();

        int options = getOptionsForText(uncorrectedText);

        if (uncorrectedText.length() > MAX_TEXT_LENGTH) {

            List<String> textForParts = splitTextOnPart(uncorrectedText, MAX_TEXT_LENGTH);

            for (String partText : textForParts) {

                TextRequest textRequest = new TextRequest(partText, options, language);

                StringBuilder partTextBuilder = new StringBuilder(partText);

                correctedTextBuilder.append(processText(textRequest, partTextBuilder));
            }

            return correctedTextBuilder.toString();
        }

        correctedTextBuilder.append(uncorrectedText);

        TextRequest textRequest = new TextRequest(uncorrectedText, options, language);

        String correctedText = processText(textRequest, correctedTextBuilder);

        LOGGER.info("method correctSpellInText has finished work");

        return correctedText;
    }

    @Override
    public String processText(TextRequest textRequest, StringBuilder textBuilder) {

        LOGGER.info("method processText has started working with parameters: " +
                "text with length = {}, options = {}, language = {}",
                textRequest.text().length(), textRequest.options(), textRequest.language());

        List<List<SpellResult>> response = sendRequestOnCheckSpellText(textRequest);

        String text = processResponseFromTextSpellCheck(response, textBuilder).toString();

        LOGGER.info("method processText has finished work");

        return text;
    }

    @Override
    public List<List<SpellResult>> sendRequestOnCheckSpellText(TextRequest textRequest) {

        LOGGER.info("method sendRequestOnCheckSpellText has started working with parameters: " +
                        "text with size = {}, options = {}, language = {}",
                textRequest.text().length(), textRequest.options(), textRequest.language());

        List<List<SpellResult>> spellCheckResults = yandexSpellerService.sendTextRequest(textRequest);

        LOGGER.info("method sendRequestOnCheckSpellText has finished work");

        return spellCheckResults;
    }

    @Override
    public StringBuilder processResponseFromTextSpellCheck(List<List<SpellResult>> spellCheckResults,
                                                           StringBuilder textBuilder) {
        LOGGER.info("method processResponseFromTextSpellCheck has started working");

        int offset = 0;

        for (List<SpellResult> listSpellResult : spellCheckResults) {
            for (SpellResult spellResult : listSpellResult) {

                if (spellResult.s().isEmpty()) continue;

                // Для замены берём первое слово из подсказок
                String wordForReplace = spellResult.s().get(0);

                int startPosition = spellResult.pos() + offset;

                int endPosition = startPosition + spellResult.len();

                textBuilder.replace(startPosition, endPosition, wordForReplace);

                offset = changeOffset(spellResult.word(), wordForReplace, offset);
            }
        }

        LOGGER.info("method processResponseFromTextSpellCheck has finished work");

        return textBuilder;
    }

    @Override
    public boolean ifRegexAreInTheText(String text, String regex) {

        LOGGER.info("method ifRegexAreInTheText has started working with parameters: " +
                "text with length = {}, regex = {}", text.length(), regex);

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);

        boolean result = matcher.find();

        LOGGER.info("method ifRegexAreInTheText has finished work, result = {}", result);

        return result;
    }

    @Override
    public int changeOffset(String originalWord, String wordForReplace, int offset) {

        LOGGER.debug("method changeOffset has started working with parameters: " +
                "original word = {}, word for replace = {}, offset = {}",
                originalWord, wordForReplace, offset);

        if (wordForReplace.length() > originalWord.length()) {
            offset += wordForReplace.length() - originalWord.length();
        }

        if (wordForReplace.length() < originalWord.length()) {
            offset -= originalWord.length() - wordForReplace.length();
        }

        LOGGER.debug("method changeOffset has finished work, offset = {}",
                offset);

        return offset;
    }

    @Override
    public List<String> splitTextOnPart(String text, int maxLengthPart) {

        LOGGER.debug("method splitTextOnPart has started working with parameters: " +
                        "text with length = {}", text.length());

        List<String> textFromParts = new ArrayList<>();

        int index = 0;

        while (index < text.length()) {
            textFromParts.add(text.substring(index,
                    Math.min(index + maxLengthPart, text.length())));
            index += maxLengthPart;
        }

        LOGGER.debug("method splitTextOnPart has finished work, " +
                        "text is divided into {} parts", textFromParts.size());

        return textFromParts;
    }

    @Override
    public int getOptionsForText(String text) {

        LOGGER.debug("method getOptionsForText has started working with parameters: " +
                "text with length = {}", text.length());

        int options = 0;

        if (ifRegexAreInTheText(text, regexForCheckNumbersInText)) {
            options += IGNORE_DIGITS;
        }

        if (ifRegexAreInTheText(text, regexForCheckUrlsInText)) {
            options += IGNORE_URLS;
        }

        LOGGER.debug("method getOptionsForText has finished work, " +
                        "options = {}", options);

        return options;
    }
}
