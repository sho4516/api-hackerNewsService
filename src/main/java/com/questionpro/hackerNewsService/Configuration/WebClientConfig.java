package com.questionpro.hackerNewsService.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${com.serviceClient.hackerNews.baseUrl}") String baseUrl) {
        int timeout = 15000; // 15 seconds

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(java.time.Duration.ofMillis(timeout));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .build();
    }
}
