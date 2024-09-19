package com.example.demo.Global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "s3 이미지 삭제 실패."),


    // 400
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST, "사용중인 아이디 입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"사용중인 닉네임 입니다."),
    INVALID_ADMIN_KEY(HttpStatus.BAD_REQUEST, "admin_key를 확인해주세요."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "아이디가 일치하지 않습니다."),

    UNFORMED_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 형식과 일치하지 않습니다."),

    NONEXISTENT_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글이 존재하지 않습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "type을 확인해주세요."),
    NONEXISTENT_BOARD(HttpStatus.BAD_REQUEST,"해당 게시물이 없습니다"),


    // 401
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"이미지 저장 중 내부 서버에서 문제가 발생했습니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");

//    // 403
//    FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),
//    FORBIDDEN_OWNER(HttpStatus.FORBIDDEN,"사장 권한이 없습니다."),
//    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN,"관리자 권한이 없습니다."),
//    FORBIDDEN_CAFE(HttpStatus.FORBIDDEN, "해당 카페에 권한이 없습니다.");






    //404 NOT_FOUND,
//    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
//    CAFE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 카페가 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}



