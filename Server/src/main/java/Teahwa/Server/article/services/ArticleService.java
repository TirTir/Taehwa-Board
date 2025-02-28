package Teahwa.Server.article.services;

import Teahwa.Server.article.dto.ArticleConverter;
import Teahwa.Server.article.dto.ArticleResponseDto;
import Teahwa.Server.article.dto.CreateRequestDto;
import Teahwa.Server.article.dto.UpdateRequestDto;
import Teahwa.Server.article.entity.Article;
import Teahwa.Server.article.repository.ArticleRepository;
import Teahwa.Server.auth.dto.UserCustom;
import Teahwa.Server.common.constants.ErrorCode;
import Teahwa.Server.common.exceptions.GeneralException;
import Teahwa.Server.user.entity.User;
import Teahwa.Server.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public void createArticle(UserCustom userCustom, CreateRequestDto request) {
        // 계정 여부 확인
        User user = userRepository.findByUserName(userCustom.getUserName())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Article newArticle = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .user(user)
                .build();

        articleRepository.save(newArticle);
    }

    // READ
    public List<ArticleResponseDto> getArticleList() {
        List<Article> articles = articleRepository.findAll();
        return ArticleConverter.toResponseList(articles);
    }

    public ArticleResponseDto getArticleDetail(int id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.ARTICLE_NOT_FOUND));
        return ArticleConverter.toResponse(article);
    }

    // UPDATE
    public void updateArticle(int articleId, UpdateRequestDto request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorCode.ARTICLE_NOT_FOUND));

        article.updateArticle(request.getTitle(), request.getContent());
        articleRepository.save(article);
    }

    // DELETE
    public void deleteArticle(int articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorCode.ARTICLE_NOT_FOUND));

        articleRepository.delete(article);
    }
}
