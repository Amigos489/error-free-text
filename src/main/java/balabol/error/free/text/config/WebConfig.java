package balabol.error.free.text.unit.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan("balabol.error.free.text")
public class WebConfig {

    @Value("${yandex.speller.url}")
    private String yandexSpellerUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(yandexSpellerUrl).
                build();
    }

    @Bean
    public JsonMapper jsonMapper() {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        return jsonMapper;
    }
}
