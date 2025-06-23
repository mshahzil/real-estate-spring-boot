package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squarenine.squarenine.entity.EventEntity;

public interface EventService {
	Optional<EventEntity> getEvent(Long id);

	List<EventEntity> getEvents();

	EventEntity addEvent(String event_str, MultipartFile[] files) throws JsonProcessingException;
	
	EventEntity addEventJson(EventEntity eventEntity);

	EventEntity addEventImages(Long eventId, MultipartFile[] files);
}
