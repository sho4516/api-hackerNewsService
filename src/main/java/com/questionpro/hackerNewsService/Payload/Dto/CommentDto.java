package com.questionpro.hackerNewsService.Payload.Dto;

import lombok.Data;

@Data
public class CommentDto {
    private String text;
    private String userHandle;
}
