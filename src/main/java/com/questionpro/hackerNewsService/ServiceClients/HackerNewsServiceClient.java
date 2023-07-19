package com.questionpro.hackerNewsService.ServiceClients;

import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface HackerNewsServiceClient {
    Mono<List<Integer>> getStoryIds();
    Mono<ItemDto> getStoryDetails(int storyId);
}
