package com.squarenine.squarenine.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squarenine.squarenine.entity.NewsEntity;
import com.squarenine.squarenine.repository.NewsRepository;
import com.squarenine.squarenine.service.NewsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    private final NewsRepository newsRepository;
    
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public Optional<NewsEntity> getNews(Long id) {
        return newsRepository.findById(id) ;
    }

    @Override
    public List<NewsEntity> getNewsAll() {
        return newsRepository.findAll();
    }
    
    @Override
    public NewsEntity addNews(String news_str, MultipartFile[] files) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        NewsEntity newsEntity = mapper.readValue(news_str, NewsEntity.class);
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"news/"));
            System.out.println(file);
        }
        String imagesString = String.join(", ", imageUrls);
        newsEntity.setImage(imagesString);
        return newsRepository.save(newsEntity);
    }

    @Override
    public NewsEntity addNewsJson(NewsEntity newsEntity) {
        return newsRepository.save(newsEntity);
    }
    
    @Override
    public NewsEntity addNewsImages(Long newsId, MultipartFile[] files) {
    	Optional<NewsEntity> newsOptional = newsRepository.findById(newsId);
        NewsEntity newsEntity = newsOptional.isPresent() ? newsOptional.get() : null;
        if (newsEntity != null) {
	        ArrayList<String> imageUrls = new ArrayList<>();
	        for (MultipartFile file:files) {
	            imageUrls.add(uploadFile(file,"news/"));
	            System.out.println(file);
	        };
	        String imagesString = String.join(", ", imageUrls);
	        newsEntity.setImage(imagesString);
	        newsRepository.save(newsEntity);
        }
        return newsEntity;
    }
    
    public String uploadFile(MultipartFile file, String path) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = path + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
        	log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
