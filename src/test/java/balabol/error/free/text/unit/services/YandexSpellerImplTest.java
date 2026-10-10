package balabol.error.free.text.unit.services;

import balabol.error.free.text.dtos.SpellResult;
import balabol.error.free.text.dtos.TextRequest;
import balabol.error.free.text.enums.LanguageType;
import balabol.error.free.text.exceptions.TextCorrectionException;
import balabol.error.free.text.services.impl.YandexSpellerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DisplayName("unit tests for YandexSpellerImpl")
public class YandexSpellerImplTest {

    private static final TextRequest TEXT_REQUEST = new TextRequest("Превет", 0, LanguageType.RU);

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    private static List<List<SpellResult>> LIST_SPELL_RESULTS;

    private static MockWebServer mockWebServer;

    private YandexSpellerServiceImpl yandexSpellerService;

    @BeforeAll
    static void setBeforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        initializeListSpellResult();
    }

    @BeforeEach
    void setBeforeEach() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        yandexSpellerService = new YandexSpellerServiceImpl(webClient, JSON_MAPPER);
    }

    @AfterAll
    static void setAfterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("When method sendTextRequest Then list spell results")
    public void sendTextRequest_listSpellResults() throws JsonProcessingException {

        List<List<SpellResult>> expectedListSpellResults = LIST_SPELL_RESULTS;

        mockWebServer.enqueue(new MockResponse()
                .setBody(JSON_MAPPER.writeValueAsString(expectedListSpellResults))
                .setResponseCode(200));

        List<List<SpellResult>> actualListSpellResult =
                yandexSpellerService.sendTextRequest(TEXT_REQUEST);

        Assertions.assertEquals(expectedListSpellResults, actualListSpellResult);
    }

    @Test
    @DisplayName("Given WebClient failed to connect When method sendTextRequest Then TextCorrectionException")
    public void sendTextRequest_exception() throws JsonProcessingException {

        List<List<SpellResult>> expectedListSpellResults = LIST_SPELL_RESULTS;

        mockWebServer.enqueue(new MockResponse()
                .setBody(JSON_MAPPER.writeValueAsString(expectedListSpellResults))
                .setResponseCode(500));

        Assertions.assertThrows(TextCorrectionException.class,
                () -> yandexSpellerService.sendTextRequest(TEXT_REQUEST));
    }

    public static void initializeListSpellResult() {
        LIST_SPELL_RESULTS = new ArrayList<>();
        LIST_SPELL_RESULTS.add(new ArrayList<>());
        LIST_SPELL_RESULTS.get(0).add(
                new SpellResult(1, 0, 0,0,6,"Превет",
                        new ArrayList<>()));
        LIST_SPELL_RESULTS.get(0).get(0).s().add("Привет");
    }
}
