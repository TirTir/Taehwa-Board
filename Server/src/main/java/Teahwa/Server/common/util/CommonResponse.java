package Teahwa.Server.common.util;

import Teahwa.Server.common.constants.SuccessCode;
import lombok.Data;

// 공통 응답
@Data
public class CommonResponse<T> {
    private final Boolean success;
    private final String message;

    protected CommonResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static CommonResponse res(Boolean success, SuccessCode status) {
        return new CommonResponse(success, status.getMessage());
    }
}
