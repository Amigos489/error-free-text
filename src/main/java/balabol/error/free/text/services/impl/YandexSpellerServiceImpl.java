package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.services.YandexSpellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class YandexSpellerServiceImpl implements YandexSpellerService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(YandexSpellerServiceImpl.class.getName());

    private final WebClient webClient;

    public YandexSpellerServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<SpellResult> sendTextForCorrection(String[] text, int options) {

        TextRequest textRequest = new TextRequest(text, options);

        ObjectMapper objectMapper = new JsonMapper();

        String requestStr = objectMapper.writeValueAsString(textRequest);

        LOGGER.info(requestStr);

        List<SpellResult> spellResults = new ArrayList<>();

        String response = webClient.post().contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("text", "превет"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);

        return spellResults;
    }

    @Override
    public void sendTextRequest(String[] text, int options) {

        String[] textTest = new String[5];
        textTest[0] = "Превет";
        textTest[1] = "Дражбан";
        textTest[2] = "Драгаценности";
        textTest[3] = "Палната";
        textTest[4] = "Приувилечение";

        JsonMapper mapper = new JsonMapper();

        TextRequest textRequest = new TextRequest(textTest, options);

        String requestStr = mapper.writeValueAsString(textRequest);

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("text", textTest);
        bodyBuilder.part("options", 0);
        MultiValueMap<String, HttpEntity<?>> parts = bodyBuilder.build();

        Mono<String> response = webClient.post()
                .body(BodyInserters.fromFormData("text", "[\"превет\", \"бритик\"]").with("options", "0"))
                .retrieve()
                .bodyToMono(String.class);

        String responseStr = response.block();
        LOGGER.info("Всё чётко! = {}", responseStr);

        List<List<SpellResult>> spellResults = mapper.readValue(responseStr, new TypeReference<List<List<SpellResult>>>() {});

        LOGGER.info("Всё чётко!!!!!!!!!!!!!! - {} - !!!!!!!!!!!!!!!!!!!!!!!!!", spellResults.get(0).get(0).s());
    }

    @Override
    public List<List<SpellResult>> sendTextResult(List<String> text, int options) {

        JsonMapper mapper = new JsonMapper();

        String textJson = mapper.writeValueAsString(text).substring(2, text.get(0).length() - 2);

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

        List<List<SpellResult>> res = mapper.readValue(response,
                new TypeReference<List<List<SpellResult>>>() {});
        return res;
    }
}
