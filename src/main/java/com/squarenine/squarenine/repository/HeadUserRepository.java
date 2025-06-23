package com.squarenine.squarenine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.HeadUserEntity;

@Repository
public interface HeadUserRepository extends JpaRepository<HeadUserEntity, Long> {
	@Query("Select c from HeadUserEntity c where c.uid = :uid")
	Optional<HeadUserEntity> findByUid(String uid);
	  
	@Query("Select c from HeadUserEntity c where c.email = :email")
	Optional<HeadUserEntity> findByEmail(String email);
}
