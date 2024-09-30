package com.example.demo.Service;


import com.example.demo.Entity.Board;
import com.example.demo.Entity.Heart;
import com.example.demo.Entity.User;
import com.example.demo.Global.exception.CustomException;
import com.example.demo.Global.exception.ErrorCode;
import com.example.demo.Global.util.Message;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.HeartRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<Message> plusHeart(Long boardId, User user){
        Board board = boardRepository.findBoardById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_BOARD)
        );
        User newUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.UNAUTHORIZED_USER)
        );
        Heart heart = heartRepository.findByBoardAndUser(board,newUser).orElse(null);

        if(heart == null){
            Heart newHeart = new Heart(board,newUser);
            heartRepository.save(newHeart);
            board.plusLikes();// 게시물 좋아요 수 증가
            boardRepository.save(board); //변경된 게시물 정보 저장
            return new ResponseEntity<>(new Message("좋아요 성공",null),HttpStatus.OK);

        }
        else{
            return new ResponseEntity<>(new Message("좋아요 존재",null),HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<Message> minusHeart(Long boardId, User user){
        Board board = boardRepository.findBoardById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_BOARD)
        );
        User newUser = userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.UNAUTHORIZED_USER)
        );
        Heart heart = heartRepository.findByBoardAndUser(board,newUser).orElse(null);
        if(heart != null){
            heartRepository.delete(heart);
            board.minusLikes();
            boardRepository.save(board);
            return new ResponseEntity<>(new Message("좋아요 취소 성공", null), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new Message("좋아요가 되어 있지 않습니다.", null), HttpStatus.BAD_REQUEST);
        }
        }

    }




