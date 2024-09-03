package com.example.demo.Global.jwt;

import com.example.demo.Dto.TokenDto;
import com.example.demo.Entity.UserRoleEnum;
import com.example.demo.Global.redis.RedisUtil;
import com.example.demo.Global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String REFRESH_KEY = "REFRESH_KEY";
    public static final long ACCESS_TIME = 60 * 60 * 1000L;
    public static final long ACCESS_COOKIE = 60 * 60;
    public static final long REFRESH_TIME = 14 * 24 * 60 * 60 * 1000L;
    public static final long REFRESH_COOKIE = 14 * 24 * 60 * 60;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisUtil redisUtil;


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    //private final RedisUtil redisUtil;

    public TokenDto creatAllToken(String username, UserRoleEnum userRole){
        return new TokenDto(createToken(username, userRole, "Access"), createToken(username, userRole, "Refresh"));
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request, String token) {
        String tokenName = token.equals("ACCESS_KEY") ? ACCESS_KEY : REFRESH_KEY;
        String bearerToken = request.getHeader(tokenName);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean checkAdminKey(String adminKey) {
        return secretKey.equals(adminKey);
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role, String tokenName) {
        Date date = new Date();
        long tokenTime = tokenName.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + tokenTime))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        if(token == null || redisUtil.hasKeyBlackList(token))
            return false;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public String getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //RefreshToken 검증
    public boolean refreshTokenValid(String token) {
        if (!validateToken(token)) return false;
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

//		Claims claims1 = Jwts.
        String userId = claims.getSubject();
        String refreshToken;
        if (redisUtil.get(userId).isPresent()) {
            refreshToken = redisUtil.get(userId).get().toString().replaceAll("\"", "").substring(7);
        } else {
            return false;
        }
        return token.equals(refreshToken);
    }

    public long getExpirationTime(String token) {
        // 토큰에서 만료 시간 정보를 추출
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // 현재 시간과 만료 시간의 차이를 계산하여 반환
        Date expirationDate = claims.getExpiration();
        Date now = new Date();
        return (expirationDate.getTime() - now.getTime());
    }

    public ResponseCookie createCookie(String name, String value) {
        long tokenTime = name.equals(ACCESS_KEY) ? ACCESS_COOKIE : REFRESH_COOKIE;
        return ResponseCookie.from(name, value)
                .path("/")
                .domain("cuping.net")
                .maxAge(tokenTime)
                .sameSite("None")
                .secure(true)
                .build();
    }

    public void setCookies(HttpServletResponse response, TokenDto tokenDto) {
//		response.addHeader("Set-Cookie", createCookie(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken()).toString());
//		response.addHeader("Set-Cookie", createCookie(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken()).toString());
        response.addHeader(ACCESS_KEY, tokenDto.getAccessToken());
        response.addHeader(REFRESH_KEY, tokenDto.getRefreshToken());
    }
}
