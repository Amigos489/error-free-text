package balabol.error.free.text.services.impl;

import balabol.error.free.text.services.YandexSpellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class YandexSpellerServiceImpl implements YandexSpellerService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(YandexSpellerServiceImpl.class.getName());

    @Override
    public void sendTextForCorrection(String textForCorrection) {

    }
}
