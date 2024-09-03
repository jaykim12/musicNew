package com.example.demo.Dto;

import com.example.demo.Entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String filename;
    private String filepath;

    // 생성자에서 this 키워드를 사용하여 필드를 초기화하는 예제
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.filename = board.getFilename();
        this.filepath = board.getFilePath();
    }
}
