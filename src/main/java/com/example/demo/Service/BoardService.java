package com.example.demo.Service;

import com.example.demo.Dto.BoardListResponse;
import com.example.demo.Dto.BoardRequestDto;
import com.example.demo.Dto.BoardResponseDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.User;
import com.example.demo.Global.exception.CustomException;
import com.example.demo.Global.exception.ErrorCode;
import com.example.demo.Global.util.Message;
import com.example.demo.Repository.BoardRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final AmazonS3Client amazonS3Client;

    private final AmazonS3 amazonS3;
    private String S3Bucket = "musicgurume";


    public List<BoardListResponse> findAllBoards(){

        return boardRepository.findAll()
                .stream()
                .map(BoardListResponse ::from)
                .sorted(Comparator.comparing(BoardListResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());

    }
    @Transactional
    public BoardListResponse searchIdBoard(Long boardId){

        Board board = findBoardById(boardId);

        return BoardListResponse.from(board);
    }




    @Transactional
    public ResponseEntity<Message> uploadMusic(MultipartFile file, User user, BoardRequestDto requestDto){
        String filePath = uploadFile(file);

        Board board = new Board(
                requestDto.getTitle(),
                requestDto.getContent(),
                file.getOriginalFilename(),
                filePath,
                user,
                user.getUsername(),
                requestDto.getCreatedAt(),
                requestDto.getModifiedAt()

        );
        boardRepository.save(board);

        return new ResponseEntity<>(new Message("게시물 생성완료", new BoardResponseDto(board)), HttpStatus.OK);
    }

    public ResponseEntity<Message> updateBoard(User user, Long boardId,BoardRequestDto requestDto,MultipartFile file){
        Board board = findBoardById(boardId);
        throwIfDontMatchId(board,user.getUserId());

        if(file == null){
            requestDto.setFilepath(board.getFilePath());
            requestDto.setFilename(board.getFilename());

        }
        else{
            deleteFile(board);
            requestDto.setFilepath(uploadFile(file));
            requestDto.setFilename(file.getOriginalFilename());
        }

        board.updateEntityBoard(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getFilename(),
                requestDto.getFilepath()

        );
        boardRepository.save(board);
        return new ResponseEntity<>(new Message("게시물 수정완료",new BoardResponseDto(board)),HttpStatus.OK);
    }

    public ResponseEntity<Message> deleteBoard(User user, Long boardId){
        Board board  = findBoardById(boardId);
        throwIfDontMatchId(board,user.getUserId());
        deleteFile(board);
        boardRepository.delete(board);
        return new ResponseEntity<>(new Message("게시글 삭제완료"),HttpStatus.OK);
    }

    private String uploadFile(MultipartFile file){
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        long size = file.getSize();
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(size);

        try{
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket,filename,file.getInputStream(),objectMetadata)
//                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (IOException e){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        }
        return amazonS3Client.getUrl(S3Bucket, filename).toString();
    }


    private Board  findBoardById( Long boardId){

        return boardRepository.findBoardById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ID)
        );
    }

    private void deleteFile(Board board){
//        String [] fileId = board.getFilePath().split("/");
//        amazonS3Client.deleteObject(S3Bucket,fileId[fileId.length-1]);
        String filePath = board.getFilePath();
        String filekey;
        try {
            if (!filePath.startsWith("http://") && !filePath.startsWith("https://")) {
                throw new MalformedURLException("잘못된 URL 형식입니다: " + filePath);
            }
            String path = new URL(filePath).getPath();
            filekey = path.substring(1);
            filekey = URLDecoder.decode(filekey, StandardCharsets.UTF_8);
            System.out.println("Extract key = " + filekey);
        }catch (MalformedURLException e){
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // S3에서 파일 삭제
        try {
            amazonS3Client.deleteObject(S3Bucket,filekey);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }


    }

    private void throwIfDontMatchId(Board board, String loginUserId){
        if(!board.getUser().getUserId().equals(loginUserId)){
            throw new CustomException(ErrorCode.INVALID_ID);
        }
    }









}