package com.squarenine.squarenine.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squarenine.squarenine.entity.AgentEntity;
import com.squarenine.squarenine.entity.AgentTaskFormEntity;
import com.squarenine.squarenine.entity.AreaEntity;
import com.squarenine.squarenine.entity.HeadUserEntity;
import com.squarenine.squarenine.repository.AgentRepository;
import com.squarenine.squarenine.repository.AgentTaskFormRepository;
import com.squarenine.squarenine.repository.AreaRepository;
import com.squarenine.squarenine.repository.HeadUserRepository;
import com.squarenine.squarenine.service.HeadUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeadUserServiceImpl implements HeadUserService {

  private AgentRepository agentRepository;
  private AreaRepository areaRepository;
  private HeadUserRepository headUserRepository;
  private AgentTaskFormRepository agentTaskFormRepository;

  public HeadUserServiceImpl(AgentRepository agentRepository, AreaRepository areaRepository,
		  HeadUserRepository headUserRepository, AgentTaskFormRepository agentTaskFormRepository) {
    this.agentRepository = agentRepository;
    this.areaRepository = areaRepository;
    this.headUserRepository = headUserRepository;
    this.agentTaskFormRepository = agentTaskFormRepository;
  }

  @Override
  public JsonNode assignArea(JsonNode jsonNode) throws JsonProcessingException {
	ObjectNode jsonNodeResp = JsonNodeFactory.instance.objectNode();
    String agentIdStr = jsonNode.get("agentId") != null ? jsonNode.get("agentId").asText() : null;
	Long agentId = null;
    if (agentIdStr != null && !agentIdStr.isEmpty()) {
        try {
            agentId = Long.parseLong(agentIdStr);
        } catch (NumberFormatException e) {
            jsonNodeResp.put("error", "Invalid agentId format. It should be a numeric value.");
            return jsonNodeResp;
        }
    }
    String area = jsonNode.get("area") != null ? jsonNode.get("area").asText() : null;
    if (area == null) jsonNodeResp.put("error", "Mandatory info [area] missing");
    else if (agentIdStr == null) jsonNodeResp.put("error", "Mandatory info [agentId] missing");
    else {
	    AreaEntity areaEntity = areaRepository.findByArea(area);
	    if (areaEntity == null) jsonNodeResp.put("error", "Area '" + area + "' does not exist");
	    else {
	    	if (agentId != null) {
		        AgentEntity agent = agentRepository.findById(agentId).orElse(null);
		        if (agent == null) jsonNodeResp.put("error", "Agent with agentId '" + agentId + "' does not exist");
		        else {
		            areaEntity.setAgentEntity(agent);
		            areaRepository.save(areaEntity);
		            ObjectMapper objectMapper = new ObjectMapper();
		            jsonNodeResp.set("areaEntity", objectMapper.valueToTree(areaEntity));
		        }
        	}
	    	else {
	    		areaEntity.setAgentEntity(null);
                areaRepository.save(areaEntity);
	            ObjectMapper objectMapper = new ObjectMapper();
	            jsonNodeResp.set("areaEntity", objectMapper.valueToTree(areaEntity));
	    	}
	    }
    }
    return jsonNodeResp;
  }
  
  @Override
  public HeadUserEntity getHeadUser(String email) {
    return headUserRepository.findByEmail(email).get();
  }
  
  @Override
  public List<AreaEntity> getUnassignedAreas() {
      return areaRepository.findUnassignedAreas();
  }
  
  @Override
  public List<AgentTaskFormEntity> getAgentTaskForms(String area) {
      return agentTaskFormRepository.findByArea(area);
  }
  
}