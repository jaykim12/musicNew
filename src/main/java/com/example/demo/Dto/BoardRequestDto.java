package com.example.demo.Dto;

import com.example.demo.Entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardRequestDto {


    private String title;
    private String content;
    private String userId;

    private String filename;

    private String filepath;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;





}
