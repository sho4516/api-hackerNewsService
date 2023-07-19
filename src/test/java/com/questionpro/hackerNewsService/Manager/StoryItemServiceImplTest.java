package com.questionpro.hackerNewsService.Manager;

import com.questionpro.hackerNewsService.Entity.Item;
import com.questionpro.hackerNewsService.Exception.CustomNotFoundException;
import com.questionpro.hackerNewsService.Exception.TopTenStoriesException;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class StoryItemServiceImplTest extends TestBase{

    private static List<Integer> storyIds = new ArrayList<>();
    private static ItemDto story1;
    private static ItemDto story2;
    private static ItemDto story3;
    private static List<ItemDto> storyDetails = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        storyIds = Arrays.asList(1, 2, 3, 4, 5);
        story1 = createStory(1, 100, 1, "story", null);
        story2 = createStory(2, 200, 2, "story", null);
        story3 = createStory(2, 300, 16, "story", null);
        storyDetails = Arrays.asList(story1, story2, story3);
    }

    @BeforeEach
    public void init() {
        initMocks();
    }
    @Test
    public void getTopTenStories_StoriesFound_ShouldReturnTopTenStories() {
        // Arrange
        when(hackerNewsServiceClient.getStoryIds()).thenReturn(Mono.just(storyIds));
        when(hackerNewsServiceClient.getStoryDetails(Mockito.anyInt()))
                .thenAnswer(invocation -> {
                    int storyId = invocation.getArgument(0);
                    if (storyIds.contains(storyId)) {
                        return getStoryById(storyId, storyDetails);
                    }
                    return Mono.empty();
                });

        // Act
        Mono<List<ItemDto>> resultMono = storyItemService.getTopTenStories();

        // Assert
        StepVerifier.create(resultMono)
                .assertNext(stories -> {
                    Assertions.assertEquals(2, stories.size());
                    Assertions.assertTrue(stories.get(0).getScore() >= stories.get(1).getScore());
                })
                .verifyComplete();
    }

    @Test
    void getTopTenStories_ExceptionThrown_ShouldThrowTopTenStoriesException() {

        // Arrange
        Throwable th = new Throwable();
        when(hackerNewsServiceClient.getStoryIds()).thenReturn(Mono.error(new WebClientRequestException(
                th,
                HttpMethod.GET,
                null,
                HttpHeaders.EMPTY
        )));

        // Act
        Mono<List<ItemDto>> result = storyItemService.getTopTenStories();

        // Assert
        StepVerifier.create(result)
                .expectError(TopTenStoriesException.class)
                .verify();
        verify(hackerNewsServiceClient, never()).getStoryDetails(anyInt());
    }

    @Test
    void getPastStories_NoStoriesFound_ShouldThrowCustomNotFoundException() {

        //Arrange
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        //Act
        Mono<List<ItemDto>> result = storyItemService.getPastStories();

        //Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomNotFoundException &&
                                ((CustomNotFoundException) throwable).getStatus() == HttpStatus.NOT_FOUND)
                .verify();
        verify(itemRepository).findAll();
    }

    @Test
    void getPastStories_StoriesFound_ShouldReturnListOfItemDto() {

        //Arrange
        List<Item> mockStories = Arrays.asList(new Item(), new Item());
        when(itemRepository.findAll()).thenReturn(mockStories);

        //Act
        Mono<List<ItemDto>> result = storyItemService.getPastStories();

        //Assert
        StepVerifier.create(result)
                .expectNextMatches(itemDtoList -> itemDtoList.size() == 2) // Verify the list size
                .expectComplete()
                .verify();
        verify(itemRepository).findAll();
    }
}
