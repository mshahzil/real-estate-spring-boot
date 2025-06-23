package com.squarenine.squarenine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.AgentTaskFormEntity;

@Repository
public interface AgentTaskFormRepository extends JpaRepository<AgentTaskFormEntity, Long> {
	@Query("Select c from AgentTaskFormEntity c where c.agentEntity.id = :agentId and c.date = :date")
	AgentTaskFormEntity findByAgentAndDate(Long agentId, String date);
	
	@Query("Select c from AgentTaskFormEntity c where c.agentEntity.id = :agentId and c.area = :area and c.date = :date")
	AgentTaskFormEntity findByAgentAreaAndDate(Long agentId, String area, String date);
	
	@Query("Select c from AgentTaskFormEntity c where c.area = :area order by c.date desc")
	List<AgentTaskFormEntity> findByArea(String area);
}