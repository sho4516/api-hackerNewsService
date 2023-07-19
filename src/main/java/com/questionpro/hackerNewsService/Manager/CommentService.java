package com.questionpro.hackerNewsService.Manager;

import com.questionpro.hackerNewsService.Payload.Dto.CommentDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CommentService {
    Mono<List<CommentDto>> getCommentsByStoryId(int storyId);
}
