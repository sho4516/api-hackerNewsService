package com.questionpro.hackerNewsService.Exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Getter
public class TopTenStoriesException extends RuntimeException{

    private String message;

    public TopTenStoriesException(String message){
        super(message);
        this.message = message;
    }
}
