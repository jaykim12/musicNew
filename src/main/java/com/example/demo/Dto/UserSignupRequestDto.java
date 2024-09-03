package com.example.demo.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    private String userId;
    private String username;
    private String role;
    private String password;



}
