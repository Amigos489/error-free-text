package balabol.error.free.text.services.impl;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.exceptions.TextCorrectionException;
import balabol.error.free.text.services.YandexSpellerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.channels.ClosedChannelException;
import java.util.List;

@Service
@AllArgsConstructor
public class YandexSpellerServiceImpl implements YandexSpellerService {

    private final String ERROR_WHEN_SENDING_REQUEST_MESSAGE = "error when sending the request";

    private final String ERROR_IN_RECEIVING_RESPONSE_MESSAGE = "error when receiving response";

    private static final Logger LOGGER =
            LoggerFactory.getLogger(YandexSpellerServiceImpl.class.getName());

    private final WebClient webClient;

    private final JsonMapper jsonMapper;

    @Override
    public List<List<SpellResult>> sendTextRequest(TextRequest textRequest) {

        try {
            LOGGER.info("method sendTextRequest has started working with parameters: " +
                            "text with length = {}, options = {}, lang = {}",
                    textRequest.text().length(), textRequest.options(), textRequest.language());

            MultiValueMap<String, String> multiValueMap = convertToMultiValueMap(textRequest);

            String response = webClient.post()
                    .body(BodyInserters.fromFormData(multiValueMap))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return jsonMapper.readValue(response, new TypeReference<>() {});
        } catch (WebClientRequestException e) {
            LOGGER.error(ERROR_WHEN_SENDING_REQUEST_MESSAGE);
            throw new TextCorrectionException(ERROR_WHEN_SENDING_REQUEST_MESSAGE);
        } catch (JsonProcessingException | WebClientResponseException e) {
            LOGGER.error(ERROR_IN_RECEIVING_RESPONSE_MESSAGE);
            throw new TextCorrectionException(ERROR_WHEN_SENDING_REQUEST_MESSAGE);
        } finally {
            LOGGER.debug("method sendTextRequest has finished work");
        }
    }

    @Override
    public MultiValueMap<String, String> convertToMultiValueMap(TextRequest textRequest) {

        LOGGER.debug("method convertToMultiValueMap has started working");

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("text", textRequest.text());
        multiValueMap.add("options", String.valueOf(textRequest.options()));
        multiValueMap.add("lang", String.valueOf(textRequest.language()));

        LOGGER.debug("method convertToMultiValueMap has finished work");

        return multiValueMap;
    }
}
