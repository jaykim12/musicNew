package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Board {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    private String title;

    private String content;

    private String filename;

    private String filepath;

    private String username;




    @ManyToOne(fetch =FetchType.LAZY)
    private User user;


    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;









    public String getFilePath() {
        return filepath;
    }

    public void setFilePath(String filePath) {
        this.filepath = filePath;
    }

    public void updateEntityBoard(String title, String content, String filename,String filePath){
        this.title= title;
        this.content = content;
        this.filename = filename;
        this.filepath = filePath;


    }

    public Board(String title, String content, String filename, String filepath, User user,String username,LocalDateTime createdAt,LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.filename = filename;
        this.filepath = filepath;
        this.user = user;
        this.username = user.getUsername();
        this.createdAt = getCreatedAt();
        this.modifiedAt = getModifiedAt();
    }



}
