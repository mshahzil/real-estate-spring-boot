package com.squarenine.squarenine.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squarenine.squarenine.entity.NewsEntity;
import com.squarenine.squarenine.service.NewsService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news/get/{id}")
    private Optional<NewsEntity> getNews(@PathVariable Long id){
        return newsService.getNews(id);
    }

    @GetMapping("/news/get/all")
    private List<NewsEntity> getListings(){
        return newsService.getNewsAll();
    }
    
    @PostMapping(value="/news/add", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    private NewsEntity addNews(@RequestPart("json") String news,
                                      @RequestPart("files") MultipartFile[] files) throws JsonProcessingException {
        return newsService.addNews(news, files);
    }
    
    @PostMapping(value = "/news/addJson")
    private NewsEntity addNewsJson(@RequestBody NewsEntity newsEntity) {
        return newsService.addNewsJson(newsEntity);
    }
    
    @PostMapping(value = "/news/addImages/{newsId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private NewsEntity addNewsImages(@PathVariable Long newsId, @RequestPart("files") MultipartFile[] files) {
        return newsService.addNewsImages(newsId, files);
    }
}
