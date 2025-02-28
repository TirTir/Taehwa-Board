package Teahwa.Server.common.dto;

import Teahwa.Server.common.util.CommonResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorResponse extends CommonResponse {

    private final HttpStatus status;

    public ApiErrorResponse(String message, HttpStatus status) {
        super(false, message);
        this.status = status;
    }

    public static ApiErrorResponse res(String message, HttpStatus status) {
        return new ApiErrorResponse(message, status);
    }
}