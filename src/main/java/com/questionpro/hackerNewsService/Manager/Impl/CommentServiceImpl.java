package com.questionpro.hackerNewsService.Manager.Impl;

import com.questionpro.hackerNewsService.Exception.CustomNotFoundException;
import com.questionpro.hackerNewsService.Manager.CommentService;
import com.questionpro.hackerNewsService.Payload.Dto.CommentDto;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import com.questionpro.hackerNewsService.ServiceClients.HackerNewsServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private HackerNewsServiceClient hackerNewsServiceClient;
    private ModelMapper mapper;

    public CommentServiceImpl(HackerNewsServiceClient hackerNewsServiceClient, ModelMapper mapper) {
        this.hackerNewsServiceClient = hackerNewsServiceClient;
        this.mapper = mapper;
    }

    @Override
    public Mono<List<CommentDto>> getCommentsByStoryId(int storyId) {
        return hackerNewsServiceClient.getStoryDetails(storyId)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("No story found with given storyId - "+storyId, HttpStatus.NOT_FOUND)))
                .flatMap(item -> {
                    if (item.getKids() == null) {
                        return Mono.error(new CustomNotFoundException("No comments found on the story id "+storyId , HttpStatus.NOT_FOUND));
                    }
                    return Flux.fromIterable(item.getKids())
                            .flatMap(hackerNewsServiceClient::getStoryDetails)
                            .filter(child -> child.getType().equals("comment") && !child.isDeleted())
                            .switchIfEmpty(Mono.error(new CustomNotFoundException("All comments deleted for story - "+storyId, HttpStatus.NOT_FOUND)))
                            .flatMap(child -> getCommentChildCount(child).map(childCount -> Tuples.of(childCount, child)))
                            .collectSortedList(Comparator.comparingInt(Tuple2::getT1))
                            .map(commentList -> commentList.stream()
                                    .limit(10)
                                    .map(Tuple2::getT2)
                                    .map(this::mapToCommentDto)
                                    .collect(Collectors.toList()));
                });
    }

    public Mono<Integer> getCommentChildCount(ItemDto item) {
        if (item.getKids() != null && !item.getKids().isEmpty() && !item.isDeleted()) {
            return Flux.fromIterable(item.getKids())
                    .flatMap(hackerNewsServiceClient::getStoryDetails)
                    .filter(kidItem -> kidItem.getType().equals("comment"))
                    .flatMap(this::getCommentChildCount)
                    .reduceWith(() -> 1, Integer::sum);
        } else {
            return Mono.just(1);
        }
    }

    private CommentDto mapToCommentDto(ItemDto item) {
        CommentDto commentDto = mapper.map(item, CommentDto.class);
        commentDto.setText(item.getText());
        commentDto.setUserHandle(item.getBy());
        return commentDto;
    }
}

