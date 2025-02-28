package Teahwa.Server.user.controllers;

import Teahwa.Server.common.constants.SuccessCode;
import Teahwa.Server.common.util.CommonResponse;
import Teahwa.Server.user.dto.AuthRequestDto;
import Teahwa.Server.user.dto.RegisterRequestDto;
import Teahwa.Server.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public CommonResponse signup(@RequestBody @Valid RegisterRequestDto request){
        log.info("[회원가입 요청] ID: {}", request.getUserName());
        userService.signup(request);
        return CommonResponse.res(true, SuccessCode.SUCCESS_SIGNUP);
    }

    @PostMapping("/signin")
    public CommonResponse signin(@RequestBody @Valid AuthRequestDto request, HttpServletResponse response) {
        log.info("[로그인 요청] ID: {}", request.getUserName());
        userService.signin(request, response);
        return CommonResponse.res(true, SuccessCode.SUCCESS_SIGNIN);
    }

    @PostMapping("/logout")
    public CommonResponse logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return CommonResponse.res(true, SuccessCode.SUCCESS_LOGOUT);
    }
}
