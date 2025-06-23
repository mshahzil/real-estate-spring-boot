package com.squarenine.squarenine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.AgentEntity;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Long> {
  @Query("Select c from AgentEntity c where c.uid = :uid")
  Optional<AgentEntity> findByUid(String uid);
  
  @Query("Select c from AgentEntity c where c.email = :email")
  Optional<AgentEntity> findByEmail(String email);

  @Query("Select c from AgentEntity c where c.office = :office")
  List<AgentEntity> findByOffice(String office);

  @Query("Select c from AgentEntity c where c.id = (Select a.agentEntity.id from AreaEntity a where a.area = :area)")
  Optional<AgentEntity> findByArea(String area);

  @Query("Select c from AgentEntity c where c.id in (Select a.agentEntity.id from AreaEntity a)")
  List<AgentEntity> findByAreaAssigned();

  @Query("Select c from AgentEntity c where c.id not in (Select a.agentEntity.id from AreaEntity a where a.agentEntity.id is not null)")
  List<AgentEntity> findByAreaUnAssigned();
  
  @Query("Select c from AgentEntity c where c.office = :office and c.id = (Select a.agentEntity.id from AreaEntity a where a.area = :area)")
  Optional<AgentEntity> findByOfficeAndArea(String office, String area);
  
  @Query("Select c from AgentEntity c where c.office = :office and c.id in (Select a.agentEntity.id from AreaEntity a)")
  List<AgentEntity> findByOfficeAndAreaAssigned(String office);
  
  @Query("Select c from AgentEntity c where c.office = :office and c.id not in (Select a.agentEntity.id from AreaEntity a where a.agentEntity.id is not null)")
  List<AgentEntity> findByOfficeAndAreaUnAssigned(String office);
}
