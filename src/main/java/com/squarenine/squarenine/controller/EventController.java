package com.squarenine.squarenine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squarenine.squarenine.entity.EventEntity;
import com.squarenine.squarenine.service.EventService;
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

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/event/get/{id}")
    private Optional<EventEntity> getEvent(@PathVariable Long id){
        return service.getEvent(id);
    }

    @GetMapping("/event/get/all")
    private List<EventEntity> getEvents(){
        return service.getEvents();
    }

    @PostMapping(value="/event/add", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    private EventEntity addEvent(@RequestPart("json") String event,
                                      @RequestPart("files") MultipartFile[] files) throws JsonProcessingException {
        return service.addEvent(event, files);
    }
    
    @PostMapping(value = "/event/addJson")
    private EventEntity addEventJson(@RequestBody EventEntity eventEntity) {
        return service.addEventJson(eventEntity);
    }
    
    @PostMapping(value = "/event/addImages/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private EventEntity addEventImages(@PathVariable Long eventId, @RequestPart("files") MultipartFile[] files) {
        return service.addEventImages(eventId, files);
    }
}
