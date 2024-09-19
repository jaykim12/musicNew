package com.example.demo.Service;

import com.example.demo.Dto.TokenDto;
import com.example.demo.Dto.UserLoginRequestDto;
import com.example.demo.Dto.UserSignupRequestDto;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserRoleEnum;
import com.example.demo.Global.exception.CustomException;
import com.example.demo.Global.exception.ErrorCode;
import com.example.demo.Global.jwt.JwtUtil;
import com.example.demo.Global.redis.RedisUtil;
import com.example.demo.Global.util.Message;
import com.example.demo.Repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;


    //회원가입
    @Transactional
    public ResponseEntity<Message> signup(UserSignupRequestDto requestDto) throws Exception {
        UserRoleEnum role = checkType(requestDto.getRole());
        duplicateCheckId(Map.of("userId", requestDto.getUserId()));
        duplicateCheckUsername(Map.of("username",requestDto.getUsername()));

        User user = userRepository.save(new User(
                requestDto.getUserId(),
                requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()), role)



        );

        return new ResponseEntity<>(new Message("회원가입 성공", null), HttpStatus.OK);
    }
    // 로그인
    public ResponseEntity<Message> login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        User user = checkUserId(userLoginRequestDto.getUserId());
        checkUserPassword(userLoginRequestDto.getPassword(), user.getPassword());
        createLoginToken(user.getUserId(), user.getRole(), response);
        return new ResponseEntity<>(new Message("로그인 성공", null), HttpStatus.OK);
    }

    // 로그아웃
    public ResponseEntity<Message> logout(User user){
        setLogoutBlackList(user.getUserId());
        return new ResponseEntity<>(new Message("로그아웃 성공", null), HttpStatus.OK);
    }




    public UserRoleEnum checkType(String type) {
        UserRoleEnum role;
        switch (type) {
            case "player" -> role = UserRoleEnum.PlAYER;
            case "producer" -> {
                role = UserRoleEnum.PRODUCER;

            }
            default -> throw new CustomException(ErrorCode.INVALID_TYPE);
        }
        return role;
    }

    public ResponseEntity<Message> duplicateCheckUsername(Map<String,String> username){
        if(userRepository.findByUsername(username.get("username")).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return new ResponseEntity<>(new Message("사용가능한 닉네임입니다.",null),HttpStatus.OK);
    }
    public ResponseEntity<Message> duplicateCheckId(Map<String, String> userId) {
        if (userRepository.findByUserId(userId.get("userId")).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
        return new ResponseEntity<>(new Message("사용 가능한 아이디입니다.", null), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public User checkUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ID)
        );
    }
    public void checkUserPassword(String inputPassword, String userPassword) {
        if (inputPassword == null || !passwordEncoder.matches(inputPassword, userPassword))
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    public void createLoginToken(String userId, UserRoleEnum role, HttpServletResponse response) {
        TokenDto tokenDto = jwtUtil.creatAllToken(userId, role);
//        redisUtil.set(userId, tokenDto.getRefreshToken(), JwtUtil.REFRESH_TIME);
        jwtUtil.setCookies(response, tokenDto);
    }

    public void setLogoutBlackList(String userId) {
        String refreshToken;
        if (redisUtil.get(userId).isPresent())
            refreshToken = redisUtil.get(userId).get().toString().substring(7);
        else
            return ;
        Long expireTime = jwtUtil.getExpirationTime(refreshToken);
        redisUtil.delete(userId);
        redisUtil.setBlackList(userId, refreshToken, expireTime);
    }




}
