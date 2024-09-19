package com.example.demo.Service;

import com.example.demo.Dto.BoardListResponse;
import com.example.demo.Dto.UserSignupRequestDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.User;
import com.example.demo.Global.exception.CustomException;
import com.example.demo.Global.exception.ErrorCode;
import com.example.demo.Global.util.Message;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Message> getMyInfo(User user){

        return new ResponseEntity<>(new Message("조회 성공",user), HttpStatus.OK);
    }

    public ResponseEntity<Message> updateMyInfo(User user, UserSignupRequestDto userSignupRequestDto){
        String encodedPassword = passwordEncoder.encode(userSignupRequestDto.getPassword());
        user.userUpdate(
                userSignupRequestDto.getUserId(),
                userSignupRequestDto.getUsername(),
                encodedPassword
        );
        userRepository.save(user);

        return new ResponseEntity<>(new Message("내 정보 수정 성공",user),HttpStatus.OK);

    }

    public List<BoardListResponse> showMyBoards(User user){
        List<Board> boards = boardRepository.findByUser(user);
        return boards.stream()
                .map(BoardListResponse::from)
                .collect(Collectors.toList());
    }






}
