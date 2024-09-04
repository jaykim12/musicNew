package com.example.demo.Controller;

import com.example.demo.Dto.BoardListResponse;
import com.example.demo.Dto.BoardRequestDto;
import com.example.demo.Global.security.UserDetailsImpl;
import com.example.demo.Global.util.Message;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @GetMapping("/all")
    public ResponseEntity<List<BoardListResponse>> allList(){
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findAllBoards());

    }

    @PostMapping("/upload")
    public ResponseEntity<Message> uploadMusic(@AuthenticationPrincipal final UserDetailsImpl userDetails, BoardRequestDto requestDto, @RequestParam MultipartFile file){

        return boardService.createBoard(file,userDetails.getUser(),requestDto);
    }
    @PutMapping("/update/{boardId}")
    public ResponseEntity<Message> updateMusic(@AuthenticationPrincipal final UserDetailsImpl userDetails,@PathVariable  Long boardId,BoardRequestDto requestDto,MultipartFile file){

        return boardService.updateBoard(userDetails.getUser(),boardId,requestDto,file);

    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Message> deleteBoard(@AuthenticationPrincipal  final UserDetailsImpl userDetails,@PathVariable Long boardId){
        boardService.deleteBoard(userDetails.getUser(),boardId);

        return  ResponseEntity.status(HttpStatus.OK).body(null);

    }


}
