package Teahwa.Server.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRequestDto {
    private String title;
    private String content;
}
