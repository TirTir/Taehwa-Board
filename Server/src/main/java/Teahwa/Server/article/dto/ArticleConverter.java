package Teahwa.Server.article.dto;

import Teahwa.Server.article.entity.Article;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleConverter {
    public static ArticleResponseDto toResponse(Article article) {
        return ArticleResponseDto.builder()
                .id(article.getId())
                .userName(article.getUser().getUserName())
                .title(article.getTitle())
                .content(article.getContent())
                .updatedAt(article.getUpdateAt())
                .build();
    }

    public static List<ArticleResponseDto> toResponseList(List<Article> articles) {
        return articles.stream()
                .map(ArticleConverter::toResponse) // 개별 게시글을 변환
                .collect(Collectors.toList());
    }
}