package com.squarenine.squarenine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squarenine.squarenine.entity.ArticleEntity;
import com.squarenine.squarenine.service.ArticleService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/article/get/{id}")
    private Optional<ArticleEntity> getListing(@PathVariable Long id){
        return articleService.getArticle(id);
    }

    @GetMapping("/article/get/all")
    private List<ArticleEntity> getListings(){
        return articleService.getArticles();
    }
    
    @PostMapping(value="/article/add", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    private ArticleEntity addArticle(@RequestPart("json") String article,
                                      @RequestPart("files") MultipartFile[] files) throws JsonProcessingException {
        return articleService.addArticle(article, files);
    }
    
    @PostMapping(value = "/article/addJson")
    private ArticleEntity addArticleJson(@RequestBody ArticleEntity articleEntity) {
        return articleService.addArticleJson(articleEntity);
    }
    
    @PostMapping(value = "/article/addImages/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ArticleEntity addArticleImages(@PathVariable Long articleId, @RequestPart("files") MultipartFile[] files) {
        return articleService.addArticleImages(articleId, files);
    }
}
