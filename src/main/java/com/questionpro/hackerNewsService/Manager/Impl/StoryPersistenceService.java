package com.questionpro.hackerNewsService.Manager.Impl;

import com.questionpro.hackerNewsService.Entity.Item;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import com.questionpro.hackerNewsService.Repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class StoryPersistenceService {
    private final ItemRepository itemRepository;
    private final ModelMapper mapper;

    public StoryPersistenceService(ItemRepository itemRepository, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Mono<Void> persistStories(List<ItemDto> stories) {
        List<Item> items = stories.stream()
                .map(itemDto -> mapper.map(itemDto, Item.class))
                .collect(Collectors.toList());

        for (Item item : items) {
            Optional<Item> existingItem = itemRepository.findByStoryId(item.getStoryId());
            if (existingItem.isPresent()) {
                Item existing = existingItem.get();
                itemRepository.updateItem(existing.getStoryId(), existing.getBy(), existing.getScore(), existing.getTime(), existing.getTitle(), existing.getType(), existing.getUrl());
            } else {
                itemRepository.save(item);
            }
        }

        return Mono.empty();
    }
}
