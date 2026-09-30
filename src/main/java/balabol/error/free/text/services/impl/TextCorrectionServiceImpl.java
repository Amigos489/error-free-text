package balabol.error.free.text.services.impl;

import balabol.error.free.text.services.TextCorrectionService;
import balabol.error.free.text.services.YandexSpellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TextCorrectionServiceImpl implements TextCorrectionService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(TextCorrectionServiceImpl.class.getName());

    private final YandexSpellerService yandexSpellerService;

    public TextCorrectionServiceImpl(YandexSpellerService yandexSpellerService) {
        this.yandexSpellerService = yandexSpellerService;
    }

    @Override
    public String correctText(String uncorrectedText) {
        return "";
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
