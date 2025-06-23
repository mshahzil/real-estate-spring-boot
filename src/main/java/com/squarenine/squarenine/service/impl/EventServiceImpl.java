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
import com.squarenine.squarenine.entity.EventEntity;
import com.squarenine.squarenine.repository.EventRepository;
import com.squarenine.squarenine.service.EventService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    private final EventRepository repository;

    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<EventEntity> getEvent(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<EventEntity> getEvents() {
        return repository.findAll();
    }

    @Override
    public EventEntity addEvent(String event_str, MultipartFile[] files) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EventEntity eventEntity = mapper.readValue(event_str, EventEntity.class);
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"events/"));
            System.out.println(file);
        }
        String imagesString = String.join(", ", imageUrls);
        eventEntity.setImage(imagesString);
        return repository.save(eventEntity);
    }

    @Override
    public EventEntity addEventJson(EventEntity eventEntity) {
        return repository.save(eventEntity);
    }
    
    @Override
    public EventEntity addEventImages(Long eventId, MultipartFile[] files) {
    	Optional<EventEntity> eventOptional = repository.findById(eventId);
    	EventEntity eventEntity = eventOptional.isPresent() ? eventOptional.get() : null;
        if (eventEntity != null) {
	        ArrayList<String> imageUrls = new ArrayList<>();
	        for (MultipartFile file:files) {
	            imageUrls.add(uploadFile(file,"events/"));
	            System.out.println(file);
	        };
	        String imagesString = String.join(", ", imageUrls);
	        eventEntity.setImage(imagesString);
	        repository.save(eventEntity);
        }
        return eventEntity;
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
