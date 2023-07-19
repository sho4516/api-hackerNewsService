package com.questionpro.hackerNewsService.ServiceClients.Impl;

import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import com.questionpro.hackerNewsService.ServiceClients.HackerNewsServiceClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.*;

@Component
public class HackerNewsServiceClientImpl implements HackerNewsServiceClient {

    private WebClient webClient;
    private final String TOP_FIFTY_STORIES_URL;
    private final String ITEM_URL;

    public HackerNewsServiceClientImpl(@Value("${com.serviceClient.hackerNews.storyIds.url}") String topFiftyStoriesUrl,
                                       @Value("${com.serviceClient.hackerNews.storyDetails.url}") String itemUrl,
                                       WebClient webClient) {
        this.TOP_FIFTY_STORIES_URL = topFiftyStoriesUrl;
        this.ITEM_URL = itemUrl;
        this.webClient = webClient;
    }

    public Mono<List<Integer>> getStoryIds() {

        return webClient.get()
                .uri(TOP_FIFTY_STORIES_URL)
                .retrieve()
                .bodyToMono(Integer[].class)
                .map(Arrays::asList)
                .defaultIfEmpty(Collections.emptyList());
    }


    public Mono<ItemDto> getStoryDetails(int storyId) {
        String storyUrl = String.format(ITEM_URL, storyId);

        return webClient.get()
                .uri(storyUrl)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.isError(), clientResponse -> Mono.empty())
                .bodyToMono(ItemDto.class);
    }
}
