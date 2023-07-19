package com.questionpro.hackerNewsService.Manager;

import com.questionpro.hackerNewsService.Manager.Impl.CommentServiceImpl;
import com.questionpro.hackerNewsService.Manager.Impl.StoryItemServiceImpl;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import com.questionpro.hackerNewsService.Repository.ItemRepository;
import com.questionpro.hackerNewsService.ServiceClients.HackerNewsServiceClient;
import com.questionpro.hackerNewsService.utils.TimeUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public abstract class TestBase {

    @Mock
    protected HackerNewsServiceClient hackerNewsServiceClient;

    @Mock
    protected ItemRepository itemRepository;

    @Mock
    protected ModelMapper modelMapper;

    @Autowired
    @InjectMocks
    protected StoryItemServiceImpl storyItemService;

    @Autowired
    @InjectMocks
    protected CommentServiceImpl commentService;

    protected static ItemDto createStory(int id, int score, int timeOffset, String type, int[] kids) {
        ItemDto story = new ItemDto();
        story.setId(id);
        story.setScore(score);
        story.setTime(TimeUtil.getEpochSecond(timeOffset));
        story.setType(type);
        if(kids!=null) {
            story.setKids(Arrays.stream(kids)
                    .boxed()
                    .collect(Collectors.toList()));
        }
        return story;
    }

    protected static ItemDto createStory(int id, int score, int timeOffset, String type, int[] kids, String text, String by) {
        ItemDto story = new ItemDto();
        story.setId(id);
        story.setScore(score);
        story.setTime(TimeUtil.getEpochSecond(timeOffset));
        story.setType(type);
        if(kids!=null) {
            story.setKids(Arrays.stream(kids)
                    .boxed()
                    .collect(Collectors.toList()));
        }
        story.setText(text);
        story.setBy(by);
        return story;
    }

    protected Mono<ItemDto> getStoryById(int storyId, List<ItemDto> storyDetails) {
        return Mono.justOrEmpty(storyDetails.stream()
                .filter(story -> story.getId() == storyId)
                .findFirst()
                .orElse(null));
    }

    protected void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}
