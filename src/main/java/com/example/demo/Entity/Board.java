package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Setter
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

    public Board(String title, String content, String filename, String filepath, User user,String username) {
        this.title = title;
        this.content = content;
        this.filename = filename;
        this.filepath = filepath;
        this.user = user;
        this.username = user.getUsername();
    }



}
