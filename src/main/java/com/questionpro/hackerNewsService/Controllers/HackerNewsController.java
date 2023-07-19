package com.questionpro.hackerNewsService.Controllers;

import com.questionpro.hackerNewsService.Manager.CommentService;
import com.questionpro.hackerNewsService.Manager.StoryItemService;
import com.questionpro.hackerNewsService.Payload.Dto.CommentDto;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@EnableCaching
@CacheConfig(cacheNames = "topTenStories")
@RestController
@RequestMapping("/api/hacker-news")
public class HackerNewsController {

    private StoryItemService storyItemService;
    private RedisTemplate redisTemplate;
    private CommentService commentService;

    public HackerNewsController(StoryItemService storyItemService, RedisTemplate redisTemplate, CommentService commentService){
        this.storyItemService = storyItemService;
        this.redisTemplate = redisTemplate;
        this.commentService = commentService;
    }

    @GetMapping("/top-stories")
    public Mono<ResponseEntity<List<ItemDto>>> getTopTenStories() {
        return getTopTenStoriesFromCache()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/past-stories")
    public Mono<ResponseEntity<List<ItemDto>>> getPastStories() {
        return storyItemService.getPastStories()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/comments/{storyId}")
    public Mono<ResponseEntity<List<CommentDto>>> getComments(@PathVariable int storyId) {
        return commentService.getCommentsByStoryId(storyId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Mono<List<ItemDto>> getTopTenStoriesFromCache() {
        ValueOperations<String, List<ItemDto>> valueOperations = redisTemplate.opsForValue();
        List<ItemDto> cachedStories = valueOperations.get("topTenStories");

        if (cachedStories != null && cachedStories.size() > 0) {
            return Mono.just(cachedStories);
        } else {
            return storyItemService.getTopTenStories()
                    .doOnNext(stories -> {
                        // Cache the fetched data for 15 minutes
                        valueOperations.set("topTenStories", stories, Duration.ofMinutes(15));
                    });
        }
    }
}
