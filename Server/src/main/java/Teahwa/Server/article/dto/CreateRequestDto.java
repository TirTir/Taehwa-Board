package Teahwa.Server.article.dto;

import lombok.Data;

@Data
public class CreateRequestDto {
    private String userId; // 수정 예정
    private String title;
    private String content;
}
