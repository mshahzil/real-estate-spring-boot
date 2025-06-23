package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squarenine.squarenine.entity.ArticleEntity;

public interface ArticleService {
	Optional<ArticleEntity> getArticle(Long id);

	List<ArticleEntity> getArticles();

	ArticleEntity addArticle(String article_str, MultipartFile[] files) throws JsonProcessingException;

	ArticleEntity addArticleJson(ArticleEntity articleEntity);

	ArticleEntity addArticleImages(Long articleId, MultipartFile[] files);
}
