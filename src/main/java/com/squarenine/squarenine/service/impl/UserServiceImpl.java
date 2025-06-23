package com.squarenine.squarenine.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.squarenine.squarenine.entity.UserEntity;
import com.squarenine.squarenine.repository.UserRepository;
import com.squarenine.squarenine.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Value("${application.bucket.name}")
  private String bucketName;
  @Autowired
  private AmazonS3 s3Client;
  private UserRepository userRepository;

  private Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserEntity addUser(UserEntity user) {
    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);
    userEntity.setVerification_status("UNVERIFIED");
    return userRepository.save(userEntity);
  }

  @Override
  public Optional<UserEntity> getUser(String uid) {
    return userRepository.findByUid(uid);
  }

  @Override
  public String verifyUser(String uid, MultipartFile[] files) {
    UserEntity userEntity = userRepository.findByUid(uid).orElseThrow(()->new NotFoundException("User with this uid not found"));

    ArrayList<String> imageUrls = new ArrayList<>();
    for (MultipartFile file:files) {
      imageUrls.add(uploadFile(file));
      System.out.println(file);
    };
    String imagesString = String.join(", ", imageUrls);
    userEntity.setDocumentImages(imagesString);
    userEntity.setVerification_status("PENDING");
    userRepository.save(userEntity);
    return "Verification request sent.";
  }

  @Override
  public Optional<UserEntity> getAgent(String uid) {
    return Optional.empty();
  }

  @Override
  public Optional<UserEntity> getUserById(Long id) {
    return userRepository.findById(id);
  }

  private String uploadFile(MultipartFile file) {

    File fileObj = convertMultiPartFileToFile(file);
    String fileName = "userDocuments/"+System.currentTimeMillis() + "_" + file.getOriginalFilename();
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
