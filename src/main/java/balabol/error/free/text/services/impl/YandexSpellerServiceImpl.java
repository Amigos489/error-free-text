package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.services.YandexSpellerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    public List<List<SpellResult>> sendTextResult(List<String> text, int options) {

        String textJson = jsonMapper.writeValueAsString(text).substring(2, text.get(0).length() - 2);

        LOGGER.info(textJson);

        String optionsStr = String.valueOf(options);

        LOGGER.info("textJson: {}, options: {}",
                textJson, options);

        String response = webClient.post()
                .body(BodyInserters
                        .fromFormData("text", textJson)
                        .with("options", optionsStr))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<List<SpellResult>> res = jsonMapper.readValue(response,
                new TypeReference<List<List<SpellResult>>>() {});
        return res;
    }
}
