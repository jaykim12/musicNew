package com.example.demo.Controller;

import com.example.demo.Dto.CommentRequestDto;
import com.example.demo.Global.security.UserDetailsImpl;
import com.example.demo.Global.util.Message;
import com.example.demo.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{boardId}")
    public ResponseEntity<Message> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal final UserDetailsImpl userDetails){
        return commentService.createComment(boardId,commentRequestDto,userDetails.getUser());
    }

    @PutMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Message> updateComment(@PathVariable Long boardId,@PathVariable Long commentId,@RequestBody CommentRequestDto commentRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(boardId,commentId,commentRequestDto,userDetails.getUser());
    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Message> deleteComment(@PathVariable Long boardId,@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(boardId,commentId,userDetails.getUser());
    }

}
