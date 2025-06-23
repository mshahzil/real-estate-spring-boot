package com.squarenine.squarenine.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.squarenine.squarenine.entity.AgentEntity;
import com.squarenine.squarenine.entity.AgentTaskFormEntity;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.entity.PersonEntity;
import com.squarenine.squarenine.entity.UserEntity;
import com.squarenine.squarenine.service.AgentService;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping(value = "/agent/get")
    private ResponseEntity<ResponseWrapper> getAgent(@RequestBody JsonNode jsonNode) {
        return ResponseEntity
            .ok()
            .body(ResponseWrapper
                .builder()
                .status(Boolean.TRUE)
                .data(agentService.getAgent(jsonNode))
                .build());
    }

    @GetMapping(value = "/agent/get/{id}")
    private ResponseEntity<ResponseWrapper> getAgent(@PathVariable Long id) {
        return ResponseEntity
            .ok()
            .body(ResponseWrapper
                .builder()
                .status(Boolean.TRUE)
                .data(agentService.getAgentById(id))
                .build());
    }

    @PostMapping(value = "/agent/get/all")
    private List<AgentEntity> getAllAgents(@RequestBody JsonNode jsonNode) {
        return agentService.getAllAgents(jsonNode);
    }

    @PostMapping(value = "/agent/add")
    private JsonNode addAgent(@RequestBody AgentEntity agentEntity) {
        return agentService.addAgent(agentEntity);
    }

    @PostMapping(value = "/agent/get/users")
    private List<Optional<UserEntity>> getVerifyUsers(@RequestBody JsonNode jsonNode) {
        return agentService.getUsers(jsonNode);
    }

    @PostMapping(value = "/agent/get/listings")
    private List<ListingEntity> getListings(@RequestBody JsonNode jsonNode) {
        return agentService.getListings(jsonNode);
    }

    @PostMapping(value = "/agent/get/vlistings")
    private List<ListingEntity> getVerifyListings(@RequestBody JsonNode jsonNode) {
        return agentService.getVListings(jsonNode);
    }

    @PostMapping(value = "/agent/get/older-listings")
    private List<ListingEntity> getOlderListings(@RequestBody JsonNode jsonNode) {
        return agentService.getOlderListings(jsonNode);
    }

    @GetMapping(value = "/agent/verify-user/{id}")
    private String verifyUser(@PathVariable Long id) {
        return agentService.verifyUser(id);
    }

    @GetMapping(value = "/agent/verify-listing/{id}")
    private String verifyListing(@PathVariable Long id) {
        return agentService.verifyListing(id);
    }

    @GetMapping(value = "/agent/getAreas")
    private JsonNode getAreas3() {
        return agentService.getAreasMapping();
    }
    @GetMapping(value = "/agent/get/areas")
    private List<String> getAreas4() {
        return agentService.getAreas();
    }
    
//    @GetMapping(value = "/agent/getAreas/{id}")
//    private List<String> getAreasByAgent() {
//        return agentService.getAreasByAgent();
//    }

    @GetMapping(value = "/agent/hide-listing/{id}")
    private String hideListing(@PathVariable Long id) {
        return agentService.hideListing(id);
    }

    @PostMapping(value = "/agent/signin")
    private PersonEntity signIn(@RequestBody JsonNode jsonNode){
        return agentService.signIn(jsonNode);
    }

    @PostMapping(value = "/agent/submitTaskForm")
    private JsonNode submitAgentTaskForm(@RequestBody AgentTaskFormEntity agentTaskFormEntity) {
        try {
            return agentService.submitAgentTaskForm(agentTaskFormEntity);
        } catch (ParseException e) {
            return JsonNodeFactory.instance.objectNode().put("error", "Invalid date format (Expected format: yyyy-MM-dd)");
        }
    }
}
