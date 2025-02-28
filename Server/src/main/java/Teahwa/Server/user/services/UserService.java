package Teahwa.Server.user.services;

import Teahwa.Server.auth.services.AuthService;
import Teahwa.Server.common.constants.ErrorCode;
import Teahwa.Server.common.exceptions.GeneralException;
import Teahwa.Server.user.dto.AuthRequestDto;
import Teahwa.Server.user.dto.RegisterRequestDto;
import Teahwa.Server.user.entity.User;
import Teahwa.Server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Transactional
    public void signup(RegisterRequestDto request) {
        // 계정 중복 확인
        if(userRepository.existsByUserName(request.getUserName())){
            throw new GeneralException(ErrorCode.USERNAME_ALREADY_EXIST);
        }

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GeneralException(ErrorCode.EMAIL_ALREADY_EXIST);
        }

        User newUser = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail()) // email 추가
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);
    }

    @Transactional
    public void signin(AuthRequestDto request, HttpServletResponse response) {
        // 계정 여부 확인
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(passwordMatches){

            // 쿠키 설정
            authService.getAuthToken(user.getEmail(), response);
        } else {
            throw new BadCredentialsException(ErrorCode.PASSWORD_MISMATCH.getMessage());
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authService.removeAuthToken(request, response);
    }
}
