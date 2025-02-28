package Teahwa.Server.common.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    // JWT
    JWT_VERIFICATION(HttpStatus.UNAUTHORIZED, "토큰 검증에 실패하였습니다."),
    JWT_INVALID_SECRET_KEY(HttpStatus.BAD_REQUEST, "시크릿 키 설정이 잘못되었습니다."),
    JWT_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "이미 로그아웃된 계정입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // 회원가입
    USERNAME_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자 계정을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자를 찾을 수 없습니다."),

    // 게시글
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 게시글을 찾을 수 없습니다."),
    ARTICLE_CREATION_FAILED(HttpStatus.BAD_REQUEST, "게시글 생성에 실패하였습니다."),
    ARTICLE_FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "게시글에 대한 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 관리자에게 문의하세요.");

    private final HttpStatus status;
    private final String message;
}