package Teahwa.Server.common.exceptions;

import Teahwa.Server.common.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GeneralException.class)
    public ApiErrorResponse handleGeneralException(GeneralException e) {
        return new ApiErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(UsernameNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ApiErrorResponse handleUsernameNotFoundException(UsernameNotFoundException e) {
//        return new ApiErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ApiErrorResponse handleBadCredentialsException(BadCredentialsException e) {
//        return new ApiErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
}