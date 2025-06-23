package com.squarenine.squarenine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.RegionalHeadEntity;

@Repository
public interface RegionalHeadRepository extends JpaRepository<RegionalHeadEntity, Long> {
  @Query("Select c from RegionalHeadEntity c where c.uid = :uid")
  Optional<RegionalHeadEntity> findByUid(String uid);
}
