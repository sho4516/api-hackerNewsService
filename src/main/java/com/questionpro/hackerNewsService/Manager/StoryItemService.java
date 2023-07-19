package com.questionpro.hackerNewsService.Manager;

import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StoryItemService {
    Mono<List<ItemDto>> getTopTenStories();
    Mono<List<ItemDto>> getPastStories();
}
