package com.squarenine.squarenine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.AreaEntity;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, Long> {
  @Query("Select c from AreaEntity c where c.agentEntity.id = :id")
  List<AreaEntity> findByAgentId(Long id);
  
  @Query("Select c from AreaEntity c where c.area = :area")
  AreaEntity findByArea(String area);
  
  @Query("Select c from AreaEntity c where c.agentEntity.id is null")
  List<AreaEntity> findUnassignedAreas();
}
