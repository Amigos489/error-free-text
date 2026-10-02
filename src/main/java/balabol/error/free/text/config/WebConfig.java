package balabol.error.free.text.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class WebConfig {

    @Value("${yandex.speller.url}")
    private String yandexSpellerUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(yandexSpellerUrl).
                build();
    }
}
