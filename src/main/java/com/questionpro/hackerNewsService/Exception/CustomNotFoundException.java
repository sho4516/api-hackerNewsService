package com.questionpro.hackerNewsService.Exception;


import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@Getter
public class CustomNotFoundException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public CustomNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
