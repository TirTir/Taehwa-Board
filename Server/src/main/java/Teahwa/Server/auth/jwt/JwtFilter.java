package Teahwa.Server.auth.jwt;

import Teahwa.Server.auth.dto.UserCustom;
import Teahwa.Server.auth.entity.RefreshToken;
import Teahwa.Server.auth.repository.RefreshTokenRepository;
import Teahwa.Server.auth.util.CookieUtil;
import Teahwa.Server.auth.util.JwtUtil;
import Teahwa.Server.common.constants.ErrorCode;
import Teahwa.Server.common.exceptions.GeneralException;
import Teahwa.Server.user.entity.User;
import Teahwa.Server.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, CookieUtil cookieUtil, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/api/signup") || path.equals("/api/signin")) {
            filterChain.doFilter(request, response);  // 로그인, 회원가입 요청은 건너뛰기
            return;
        }

        if (path.equals("/article") || path.equals("/article/detail")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 access token 추출
        String accessToken = cookieUtil.getAccessTokenFromCookie(request);
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);

        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            log.info("AccessToken 유효성 검사 결과: {}", jwtUtil.validateToken(accessToken));

            // access token이 유효하면 사용자 정보 추출
            Claims claims = jwtUtil.getClaimsFromToken(accessToken);
            log.info("Claims 정보: {}", claims);

            String email = claims.getSubject();
            List<String> roles = claims.get("roles", List.class); // JWT에서 권한 정보 추출

            // 인증 객체 생성
            setSecurityContext(email, roles);

        } else if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            // refresh token이 유효한 경우 access token을 재발급
            String email = jwtUtil.getEmailFromToken(refreshToken);

            // email 기반 refresh token 확인
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByEmail(email);

            if (optionalRefreshToken.isPresent() && optionalRefreshToken.get().getRefreshToken().equals(refreshToken)) {
                // 새로운 access token 생성
                String newAccessToken = jwtUtil.generateAccessToken(email);

                // 새 access token을 쿠키에 저장
                cookieUtil.createAccessTokenCookie(response, newAccessToken, true);

                // JWT에서 권한 정보를 다시 가져와서 인증 객체 생성
                List<String> roles = jwtUtil.getRolesFromToken(newAccessToken);
                setSecurityContext(email, roles);
            } else {
                // refresh token이 DB와 일치하지 않으면 로그아웃 처리 (쿠키 삭제)
                invalidateCookies(response);
            }
        } else {
            // 둘 다 유효하지 않으면 세션 및 쿠키 삭제 (로그아웃 처리)
            invalidateCookies(response);
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(String email, List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            log.error("JWT 토큰에서 roles 정보가 없습니다.");
            return; // roles가 없으면 필터 중단
        }

        // GrantedAuthority를 List로 변환
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // UserCustom 객체 생성
        UserCustom userCustom = new UserCustom(user, authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCustom, null, userCustom.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private void invalidateCookies(HttpServletResponse response) {
        // Spring Security 컨텍스트 초기화
        SecurityContextHolder.clearContext();
        // 쿠키 삭제
        cookieUtil.deleteAccessTokenCookie(response, true);
        cookieUtil.deleteRefreshTokenCookie(response, true);
    }
}