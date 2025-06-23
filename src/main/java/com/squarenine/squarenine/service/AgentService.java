package com.squarenine.squarenine.service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.entity.AgentEntity;
import com.squarenine.squarenine.entity.AgentTaskFormEntity;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.entity.PersonEntity;
import com.squarenine.squarenine.entity.UserEntity;

public interface AgentService {
  JsonNode addAgent(AgentEntity agent);

  PersonEntity getAgent(JsonNode jsonNode);

  String verifyUser(Long id);

  String verifyListing(Long id);

  List<Optional<UserEntity>> getUsers(JsonNode jsonNode);

  List<ListingEntity> getListings(JsonNode jsonNode);

  List<AgentEntity> getAllAgents(JsonNode jsonNode);

  List<String> getAreas();
  
  JsonNode getAreasMapping();

  List<ListingEntity> getOlderListings(JsonNode jsonNode);

  String hideListing(Long id);

  List<ListingEntity> getVListings(JsonNode jsonNode);

  PersonEntity signIn(JsonNode jsonNode);

  Optional<AgentEntity> getAgentById(Long id);
  
  JsonNode submitAgentTaskForm(AgentTaskFormEntity agentTaskFormEntity) throws ParseException;
}
