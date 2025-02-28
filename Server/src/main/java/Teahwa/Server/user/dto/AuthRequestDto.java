package Teahwa.Server.user.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String userName;
    private String password;
}
