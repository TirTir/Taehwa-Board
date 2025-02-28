package Teahwa.Server.article.controllers;

import Teahwa.Server.article.dto.ArticleResponseDto;
import Teahwa.Server.article.dto.CreateRequestDto;
import Teahwa.Server.article.dto.UpdateRequestDto;
import Teahwa.Server.article.services.ArticleService;
import Teahwa.Server.auth.dto.UserCustom;
import Teahwa.Server.common.dto.ApiSuccessResponse;
import Teahwa.Server.common.util.CommonResponse;
import Teahwa.Server.common.constants.SuccessCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 게시글 작성 -> 인가 확인
    @PostMapping("/create")
    public CommonResponse createArticle(@AuthenticationPrincipal UserCustom userCustom, @RequestBody CreateRequestDto request) {
        log.info("[게시글 생성 요청] ID: {}", userCustom.getUsername());
        articleService.createArticle(userCustom, request);
        return CommonResponse.res(true, SuccessCode.SUCCESS_CREATE_ARTICLE);
    }

    // 게시글 수정 -> 인가 확인
    @PutMapping("/update/{articleId}")
    public CommonResponse updateBoard(@PathVariable int articleId, @RequestBody UpdateRequestDto request) {
        // log.info("[게시글 수정 요청] ID: {}", customUserDetails.getUsername());
        articleService.updateArticle(articleId, request);
        return CommonResponse.res(true, SuccessCode.SUCCESS_UPDATE_ARTICLE);
    }

    // 게시글 목록 조회
    @GetMapping("/")
    public ApiSuccessResponse<List<ArticleResponseDto>> getArticleList() {
        return ApiSuccessResponse.res(SuccessCode.SUCCESS_ARTICLE_LIST, articleService.getArticleList());
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{articleId}")
    public ApiSuccessResponse<ArticleResponseDto> getArticle(@PathVariable int articleId) {
        return ApiSuccessResponse.res(SuccessCode.SUCCESS_ARTICLE_DETAIL, articleService.getArticleDetail(articleId));
    }

    // 게시글 삭제 -> 인가 확인
    @DeleteMapping("/{id}")
    public CommonResponse deleteBoard(@PathVariable int id) {
        articleService.deleteArticle(id);
        return CommonResponse.res(true, SuccessCode.SUCCESS_DELETE_ARTICLE);
    }
}
