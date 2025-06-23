package com.squarenine.squarenine.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.entity.AgentTaskFormEntity;
import com.squarenine.squarenine.entity.AreaEntity;
import com.squarenine.squarenine.entity.HeadUserEntity;
import com.squarenine.squarenine.service.HeadUserService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class HeadUserController {

    private final HeadUserService headUserService;

    public HeadUserController(HeadUserService headUserService) {
        this.headUserService = headUserService;
    }
    
    @PostMapping(value = "/head/assign")
    private JsonNode assignArea(@RequestBody JsonNode jsonNode) throws JsonProcessingException {
        return headUserService.assignArea(jsonNode);
    }
    
    @GetMapping(value = "/head/getAgentTaskForms/{area}")
    private List<AgentTaskFormEntity> getAgentTaskForms(@PathVariable String area) {
        return headUserService.getAgentTaskForms(area);
    }
    
    @GetMapping(value = "/head/getUnassignedAreas")
    private List<AreaEntity> getUnassignedAreas() {
        return headUserService.getUnassignedAreas();
    }

    @GetMapping(value = "/head/get/{email}")
    private HeadUserEntity getHead(@PathVariable String email) {
        return headUserService.getHeadUser(email);
    }
}
