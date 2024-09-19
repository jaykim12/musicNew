package com.example.demo.Service;

import com.example.demo.Dto.CommentRequestDto;
import com.example.demo.Dto.CommentResponseDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.User;
import com.example.demo.Global.exception.CustomException;
import com.example.demo.Global.exception.ErrorCode;
import com.example.demo.Global.util.Message;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<Message> createComment(Long boardId, CommentRequestDto commentRequestDto, User user){
        Board board = findBoardIdOrElseThrow(boardId);

        Comment comment = new Comment(
                commentRequestDto.getContent(),
                user,
                board
        );
        commentRepository.save(comment);

        return new ResponseEntity<>(new Message("댓글 추가 성공", new CommentResponseDto(comment)), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<Message> updateComment(Long boardId,Long commentId,CommentRequestDto commentRequestDto,User user){
        Board board = findBoardIdOrElseThrow(boardId);
        Comment comment = findCommentByIdOrElseThrow(commentId);

        throwIfNotOwner(comment,user.getUsername());
        comment.updateContent(commentRequestDto.getContent());

        return new ResponseEntity<>(new Message("댓글 수정 성공",new CommentResponseDto(comment)),HttpStatus.OK);

    }

    public ResponseEntity<Message> deleteComment(Long boardId,Long commentId,User user){
        Board board = findBoardIdOrElseThrow(boardId);
        Comment comment =findCommentByIdOrElseThrow(commentId);
        throwIfNotOwner(comment, user.getUsername());
        commentRepository.delete(comment);

        return new ResponseEntity<>(new Message("댓글삭제 완료"),HttpStatus.OK);
    }




    private Comment findCommentByIdOrElseThrow(Long commentId) {

        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_COMMENT)
        );
    }

    private void throwIfNotOwner(final Comment comment, final String loginUsername) {

        if (!comment.getUser().getUsername().equals(loginUsername))
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
    }








    private Board findBoardIdOrElseThrow( Long boardId) {

        return boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.NONEXISTENT_BOARD)
        );
    }
}
