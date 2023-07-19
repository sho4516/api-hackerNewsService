package com.questionpro.hackerNewsService.Manager.Impl;

import com.questionpro.hackerNewsService.Entity.Item;
import com.questionpro.hackerNewsService.Exception.CustomNotFoundException;
import com.questionpro.hackerNewsService.Exception.TopTenStoriesException;
import com.questionpro.hackerNewsService.Manager.StoryItemService;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import com.questionpro.hackerNewsService.Repository.ItemRepository;
import com.questionpro.hackerNewsService.ServiceClients.HackerNewsServiceClient;
import com.questionpro.hackerNewsService.utils.TimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoryItemServiceImpl implements StoryItemService {

    private HackerNewsServiceClient hackerNewsServiceClient;
    private ItemRepository itemRepository;
    private ModelMapper mapper;
    private final StoryPersistenceService storyPersistenceService;

    public StoryItemServiceImpl(HackerNewsServiceClient hackerNewsServiceClient, ItemRepository itemRepository, ModelMapper mapper, StoryPersistenceService storyPersistenceService) {
        this.hackerNewsServiceClient = hackerNewsServiceClient;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.storyPersistenceService = storyPersistenceService;
    }

    @Override
    public Mono<List<ItemDto>> getTopTenStories() {

        var topTenStories = hackerNewsServiceClient.getStoryIds()
                .flatMapMany(Flux::fromIterable)
                .flatMap(hackerNewsServiceClient::getStoryDetails)
                .filter(TimeUtil::isWithinLast15Minutes)
                .filter(itemDto -> itemDto.getType().equals("story"))
                .sort(Comparator.comparingInt(ItemDto::getScore).reversed())
                .take(10)
                .collectList()
                .doOnError(exception -> {
                    if (exception instanceof WebClientRequestException) {
                        exception.printStackTrace();
                        throw new TopTenStoriesException("Error occurred while connecting with Third party service");
                    }
                })
                .doOnNext(storyPersistenceService::persistStories);

        return topTenStories;

    }

    @Override
    public Mono<List<ItemDto>> getPastStories() {
        List<Item> stories = itemRepository.findAll();

        if (stories.isEmpty()) {
            return Mono.error(new CustomNotFoundException("No stories found in the database.", HttpStatus.NOT_FOUND));
        }

        List<ItemDto> storiesDto = stories
                .stream()
                .map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());

        return Mono.just(storiesDto);
    }
}

