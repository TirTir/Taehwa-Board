package Teahwa.Server.common.exceptions;

import Teahwa.Server.common.constants.ErrorCode;
import Teahwa.Server.common.dto.ApiErrorResponse;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{

    private final ErrorCode errorCode;

    public GeneralException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiErrorResponse toErrorResponse() {
        return ApiErrorResponse.res(errorCode.getMessage(), errorCode.getStatus());
    }
}