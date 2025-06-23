package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squarenine.squarenine.entity.NewsEntity;

public interface NewsService {
	Optional<NewsEntity> getNews(Long id);

	List<NewsEntity> getNewsAll();

	NewsEntity addNews(String news_str, MultipartFile[] files) throws JsonProcessingException;

	NewsEntity addNewsJson(NewsEntity newsEntity);

	NewsEntity addNewsImages(Long newsId, MultipartFile[] files);
}