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
import com.squarenine.squarenine.entity.ArticleEntity;
import com.squarenine.squarenine.repository.ArticleRepository;
import com.squarenine.squarenine.service.ArticleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<ArticleEntity> getArticle(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<ArticleEntity> getArticles() {
        return articleRepository.findAll();
    }
    
    @Override
    public ArticleEntity addArticle(String article_str, MultipartFile[] files) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArticleEntity articleEntity = mapper.readValue(article_str, ArticleEntity.class);
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"article/"));
            System.out.println(file);
        }
        String imagesString = String.join(", ", imageUrls);
        articleEntity.setImage(imagesString);
        return articleRepository.save(articleEntity);
    }

    @Override
    public ArticleEntity addArticleJson(ArticleEntity articleEntity) {
        return articleRepository.save(articleEntity);
    }
    
    @Override
    public ArticleEntity addArticleImages(Long articleId, MultipartFile[] files) {
    	Optional<ArticleEntity> articleOptional = articleRepository.findById(articleId);
    	ArticleEntity articleEntity = articleOptional.isPresent() ? articleOptional.get() : null;
        if (articleEntity != null) {
	        ArrayList<String> imageUrls = new ArrayList<>();
	        for (MultipartFile file:files) {
	            imageUrls.add(uploadFile(file,"article/"));
	            System.out.println(file);
	        };
	        String imagesString = String.join(", ", imageUrls);
	        articleEntity.setImage(imagesString);
	        articleRepository.save(articleEntity);
        }
        return articleEntity;
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
