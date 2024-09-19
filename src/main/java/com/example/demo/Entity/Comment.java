package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)  // 추가
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private String username;

    private String title;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

    public Comment(String content,User user,Board board){
        this.content = content;
        this.user = user;
        this.board = board;
        this.username = user.getUsername();
        this.title = board.getTitle();
    }




    public void updateContent(String content){
        this.content = content;

    }



}
