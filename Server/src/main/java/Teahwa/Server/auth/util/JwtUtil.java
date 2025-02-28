package Teahwa.Server.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15분
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일
    private static final String AUTHORITIES_KEY = "auth";

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    // access token 생성
    public String generateAccessToken(String email) {
        return generateToken(email, ACCESS_TOKEN_EXPIRATION);
    }

    // refresh token 생성
    public String generateRefreshToken(String email) {
        return generateToken(email, REFRESH_TOKEN_EXPIRATION);
    }

    // 토큰 생성
    private String generateToken(String email, long expirationTime) {

        return Jwts
                .builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, "auth")
                .claim("roles", Collections.singletonList("auth"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // 토큰 클레임 검사
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();

            // 만료 시간 확인
            return !claims.getExpiration().before(Date.from(Instant.now()));
        } catch (Exception e) {
            // 예외 발생 시, 유효하지 않은 토큰으로 간주
            return false;
        }
    }

    // 토큰에서 Claims 추출
    public Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    // 토큰에서 사용자 이름 추출
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // 토큰에서 만료 시간 추출
    public Date getExpirationFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public List<String> getRolesFromToken(String newAccessToken) {
        try {
            // JWT 비밀 키를 사용하여 Claims 객체를 추출
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(newAccessToken)
                    .getBody();

            List<String> roles = claims.get("roles", List.class);

            if (claims == null || claims.get("roles") == null) {
                log.error("JWT 토큰에서 roles 정보를 찾을 수 없습니다.");
                return Collections.emptyList(); // null 대신 빈 리스트 반환
            }

            return roles != null ? roles : List.of(); // null 체크 후 반환
        } catch (Exception e) {
            // 다른 예외 처리
            throw new RuntimeException("토큰처리를 할 수 없습니다.", e);
        }
    }
}