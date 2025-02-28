package Teahwa.Server.auth.services;

import Teahwa.Server.auth.entity.BlackList;
import Teahwa.Server.auth.entity.RefreshToken;
import Teahwa.Server.auth.repository.BlackListRepository;
import Teahwa.Server.auth.repository.RefreshTokenRepository;
import Teahwa.Server.auth.util.CookieUtil;
import Teahwa.Server.auth.util.JwtUtil;
import Teahwa.Server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(JwtUtil jwtUtil, CookieUtil cookieUtil, RefreshTokenRepository refreshTokenRepository, BlackListRepository blackListRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.blackListRepository = blackListRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void getAuthToken(String email, HttpServletResponse response) {
        // access token 및 refresh token 생성
        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // JwtUtil에서 refreshToken의 만료 시간을 가져옴
        Date refreshExpDate = jwtUtil.getExpirationFromToken(refreshToken);
        LocalDateTime refreshExp = refreshExpDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<RefreshToken> existingOpt = refreshTokenRepository.findByEmail(email);
        if (existingOpt.isPresent()) {
            RefreshToken existingToken = existingOpt.get();
            existingToken.updateToken(refreshToken, refreshExp);
            refreshTokenRepository.save(existingToken);
        } else {
            RefreshToken newToken = RefreshToken.builder()
                    .email(email)
                    .refreshToken(refreshToken)
                    .expired(refreshExp)
                    .build();
            refreshTokenRepository.save(newToken);
        }

        // access token 및 refresh token을 쿠키로 설정
        cookieUtil.createAccessTokenCookie(response, accessToken, true);
        cookieUtil.createRefreshTokenCookie(response, refreshToken, true);
    }


    public void removeAuthToken(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 access token 및 refresh token 삭제
        cookieUtil.deleteAccessTokenCookie(response, true);
        cookieUtil.deleteRefreshTokenCookie(response, true);

        String accessToken = cookieUtil.getAccessTokenFromCookie(request);

        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            // access token에서 사용자 이메일 추출
            String email = jwtUtil.getEmailFromToken(accessToken);

            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(email);
            refreshToken.ifPresent(refreshTokenRepository::delete);

            Date expirationDate = jwtUtil.getExpirationFromToken(accessToken);
            LocalDateTime expiration = expirationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // AccessToken 블랙리스트에 저장
            BlackList blackListToken = BlackList.builder()
                    .accessToken(accessToken)
                    .expiration(expiration)  // JWT 만료 시간 저장
                    .build();

            blackListRepository.save(blackListToken);
        }
    }
}