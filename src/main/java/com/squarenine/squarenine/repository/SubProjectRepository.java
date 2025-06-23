package com.squarenine.squarenine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.SubProjectEntity;

@Repository
public interface SubProjectRepository extends JpaRepository<SubProjectEntity,Long> {
  @Query("SELECT u FROM SubProjectEntity u WHERE u.project_id = ?1")
  List<SubProjectEntity> findByPId(Long id);
}
