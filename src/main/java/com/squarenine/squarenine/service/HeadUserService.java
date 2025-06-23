package com.squarenine.squarenine.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.entity.AgentTaskFormEntity;
import com.squarenine.squarenine.entity.AreaEntity;
import com.squarenine.squarenine.entity.HeadUserEntity;

public interface HeadUserService {

  JsonNode assignArea(JsonNode jsonNode) throws JsonProcessingException;
  
  HeadUserEntity getHeadUser(String jsonNode);
  
  List<AreaEntity> getUnassignedAreas();
  
  List<AgentTaskFormEntity> getAgentTaskForms(String area);

}