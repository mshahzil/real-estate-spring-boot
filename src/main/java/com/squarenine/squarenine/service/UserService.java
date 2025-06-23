package com.squarenine.squarenine.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.squarenine.squarenine.entity.UserEntity;

public interface UserService {
  UserEntity addUser(UserEntity user);

  Optional<UserEntity> getUser(String uid);

  String verifyUser(String uid, MultipartFile[] files);

  Optional<UserEntity> getAgent(String uid);

  Optional<UserEntity> getUserById(Long id);
}
