package com.example.demo.Controller;

import com.example.demo.Dto.BoardListResponse;
import com.example.demo.Dto.UserSignupRequestDto;
import com.example.demo.Global.security.UserDetailsImpl;
import com.example.demo.Global.util.Message;
import com.example.demo.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    // 내 정보 조회
    @GetMapping("/info")
    public ResponseEntity<Message> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyInfo(userDetails.getUser());
    }
    @PutMapping("/update")
    public ResponseEntity<Message> updateMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserSignupRequestDto signupRequestDto){
        return myPageService.updateMyInfo(userDetails.getUser(),signupRequestDto);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardListResponse>> showMyBoards(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(myPageService.showMyBoards(userDetails.getUser()));
    }



}
