package com.example.demo.Controller;

import com.example.demo.Dto.UserLoginRequestDto;
import com.example.demo.Dto.UserSignupRequestDto;
import com.example.demo.Global.security.UserDetailsImpl;
import com.example.demo.Global.util.Message;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;



    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto) throws Exception {
        return userService.signup( userSignupRequestDto);
    }



    @PostMapping("/checkId")
    public ResponseEntity<Message> checkId(@RequestBody Map<String, String> userId) {
        return userService.duplicateCheckId(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody UserLoginRequestDto userLoginRequestDto
            , HttpServletResponse response) {
        return userService.login(userLoginRequestDto, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Message>logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.logout(userDetails.getUser());
    }
}
