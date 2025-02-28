package Teahwa.Server.common.dto;

import Teahwa.Server.common.constants.SuccessCode;
import Teahwa.Server.common.util.CommonResponse;
import lombok.Getter;

@Getter
public class ApiSuccessResponse<T> extends CommonResponse {

    private final T data;

    private ApiSuccessResponse(String message, T data) {
        super(true, message);
        this.data = data;
    }

    public static<T> ApiSuccessResponse<T> res(SuccessCode status, T data) {
        return new ApiSuccessResponse<>(status.getMessage(), data);
    }
}