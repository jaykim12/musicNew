package com.example.demo.Dto;

import com.example.demo.Entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
public class BoardListResponse {

    private Long id;
    private String title;
    private String content;

    private String username;

    private String filename;

    private String filepath;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
    public BoardListResponse(Long id, String title, String content, String username,
                             String filename, String filepath,
                             LocalDate createdAt, LocalDate modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.filename = filename;
        this.filepath = filepath;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static BoardListResponse from (Board board){

        return new BoardListResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getUsername(),
                board.getFilename(),
                board.getFilePath(),
                board.getCreatedAt(),
                board.getModifiedAt()

        );

    }

}
