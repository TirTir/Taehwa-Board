package Teahwa.Server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 2, max = 8, message = "아이디는 2~8자 사이로 입력해주세요.")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&.])[A-Za-z\\d$@$!%*#?&.]{8,20}$",
            message = "비밀번호는 영문자와 특수문자를 포함하여 8자 이상 20자 이하로 입력해주세요."
    )
    private String password;
}
