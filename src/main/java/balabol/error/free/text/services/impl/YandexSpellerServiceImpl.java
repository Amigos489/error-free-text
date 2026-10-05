package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.exceptions.TextCorrectionException;
import balabol.error.free.text.services.YandexSpellerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class YandexSpellerServiceImpl implements YandexSpellerService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(YandexSpellerServiceImpl.class.getName());

    private final WebClient webClient;

    private final JsonMapper jsonMapper;

    @Override
    public List<List<SpellResult>> sendTextRequest(TextRequest textRequest) {

        LOGGER.info("method sendTextRequest has started working with parameters: {}",
                textRequest.toString());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("text", textRequest.text());
        multiValueMap.add("options", String.valueOf(textRequest.options()));

        String response = webClient.post()
                .body(BodyInserters.fromFormData(multiValueMap))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(error -> {
                    LOGGER.error(error.getMessage());
                    throw new TextCorrectionException(error.getMessage());
                }).block();

        LOGGER.info("method sendTextRequest has finished work, response = {}",
                response);

        return jsonMapper.readValue(response,
                new TypeReference<List<List<SpellResult>>>() {});
    }
}
