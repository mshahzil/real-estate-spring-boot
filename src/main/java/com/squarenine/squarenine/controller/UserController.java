package com.squarenine.squarenine.controller;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.entity.UserEntity;
import com.squarenine.squarenine.service.UserService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/add")
    private UserEntity addUser(@RequestBody UserEntity user){
        return userService.addUser(user);
    }

    @PostMapping("/user/get")
    private Optional<UserEntity> getUser(@RequestBody JsonNode node){
        return userService.getUser(node.get("uid").asText());
    }

    @PostMapping(value = "/user/verify",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    private String verifyUser(@RequestPart("uid") String uid,
        @RequestPart("files") MultipartFile[] files) {
        uid = uid.replace("\"", "");
        return userService.verifyUser(uid, files);
    }

    @GetMapping(value = "/user/get/{id}")
    private Optional<UserEntity> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
}
