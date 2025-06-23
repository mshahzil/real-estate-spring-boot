package com.squarenine.squarenine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  @Query("Select c from UserEntity c where c.uid = :uid")
  Optional<UserEntity> findByUid(String uid);

  @Query("Select c from UserEntity c where c.area = :area")
  List<Optional<UserEntity>> findByArea(String area);
  
  @Query("Select c from UserEntity c where c.verification_status in ('PENDING', 'VERIFIED')")
  List<Optional<UserEntity>> findByVerificationStatus();
}
