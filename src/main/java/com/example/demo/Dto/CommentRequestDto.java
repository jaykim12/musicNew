package com.example.demo.Dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentRequestDto {

    private String content;

    private LocalDate createdAt;
    private LocalDate modifiedAt;
}
