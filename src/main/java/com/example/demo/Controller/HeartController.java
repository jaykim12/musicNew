package com.example.demo.Controller;

import com.example.demo.Global.security.UserDetailsImpl;
import com.example.demo.Global.util.Message;
import com.example.demo.Service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hearts")
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/plus/{boardId}")
    public ResponseEntity<Message> pluslike(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return heartService.plusHeart(boardId,userDetails.getUser());
    }
    @DeleteMapping("/minus/{boardId}")
    public ResponseEntity<Message> minuslike(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return heartService.minusHeart(boardId,userDetails.getUser());
    }



}
