package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    //Enumerated 어노테이션 사용해서 문자열 형태로 데이터베아스에 저장 -> 열거형 순서 변경되더라도 데이터 무결성 유지

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Board> boards = new ArrayList<>();






    //일반 회원가입 생성자
    @Builder
    public User(String userId, String username, String password, UserRoleEnum role,LocalDate createdAt,LocalDate modifiedAt){
        this.userId =userId;
        this.username =username;
        this.password = password;
        this.role = role;
        this.createdAt = getCreatedAt();
        this.modifiedAt = getModifiedAt();



    }

    public void userUpdate(String userId,String username,String password){
        this.userId = userId;
        this.username = username;
        this.password =password;
    }

}
