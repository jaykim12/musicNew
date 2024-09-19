package com.example.demo.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    private String userId;
    private String username;
    private String role;
    private String password;
    private LocalDate createdAt;
    private LocalDate modifiedAt;




}
