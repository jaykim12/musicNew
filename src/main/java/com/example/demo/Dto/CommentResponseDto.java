package com.example.demo.Dto;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Data
public class CommentResponseDto {
    private String content;


    private String username;
    private String title;


    private LocalDate createdAt;
    private LocalDate modifiedAt;

    public CommentResponseDto(Comment comment){
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.title = comment.getBoard().getTitle();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
