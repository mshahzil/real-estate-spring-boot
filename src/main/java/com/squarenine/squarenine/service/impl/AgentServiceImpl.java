package com.squarenine.squarenine.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squarenine.squarenine.entity.AgentEntity;
import com.squarenine.squarenine.entity.AgentTaskFormEntity;
import com.squarenine.squarenine.entity.AreaEntity;
import com.squarenine.squarenine.entity.ClientReportEntity;
import com.squarenine.squarenine.entity.HeadUserEntity;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.entity.PersonEntity;
import com.squarenine.squarenine.entity.UserEntity;
import com.squarenine.squarenine.repository.AgentRepository;
import com.squarenine.squarenine.repository.AgentTaskFormRepository;
import com.squarenine.squarenine.repository.AreaRepository;
import com.squarenine.squarenine.repository.ClientReportRepository;
import com.squarenine.squarenine.repository.HeadUserRepository;
import com.squarenine.squarenine.repository.ListingRepository;
import com.squarenine.squarenine.repository.UserRepository;
import com.squarenine.squarenine.service.AgentService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AgentServiceImpl implements AgentService {

  private UserRepository userRepository;
  private ListingRepository listingRepository;
  private AgentRepository agentRepository;
  private HeadUserRepository headUserRepository;
  private AreaRepository areaRepository;
  private AgentTaskFormRepository agentTaskFormRepository;
  private ClientReportRepository clientReportRepository;

  @Override
  public JsonNode addAgent(AgentEntity agent) {
	ObjectNode jsonNodeResp = JsonNodeFactory.instance.objectNode();
	AgentEntity agentEntity = agentRepository.findByEmail(agent.getEmail()).orElse(null);
	if (agentEntity != null) {
		jsonNodeResp.put("error", "Agent with this email already exists");
	}
	else {
		agent = agentRepository.save(agent);
		ObjectMapper objectMapper = new ObjectMapper();
		jsonNodeResp.set("agentEntity", objectMapper.valueToTree(agent));
	}
    return jsonNodeResp;
  }

  @Override
  public PersonEntity getAgent(JsonNode jsonNode) {

    if (headUserRepository.findByEmail(jsonNode.get("email").asText()).isPresent()){
      HeadUserEntity headUser = headUserRepository.findByEmail(jsonNode.get("email").asText()).get();
      headUser.setRole("HeadUser");
      return headUser;
    }
    AgentEntity agentEntity = agentRepository.findByEmail(jsonNode.get("email").asText()).orElseThrow(()->new RuntimeException("Agent Not Found"));
    agentEntity.setRole("Agent");
    String area = "";
    for (AreaEntity areaEntity : areaRepository.findByAgentId(agentEntity.getId())){
      if (area.isEmpty()) area+=areaEntity.getArea();
      else area += ", "+areaEntity.getArea();
    }
    agentEntity.setArea(area);
    return agentEntity;
  }
  @Override
  public PersonEntity signIn(JsonNode jsonNode) {
    PersonEntity entity = getAgent(jsonNode);
    if (entity.getPassword().equals(jsonNode.get("password").asText())){
      return entity;
    }
    return null;
  }

  @Override
  public String verifyUser(Long id) {
    UserEntity user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User Not Found"));
    user.setVerification_status("VERIFIED");
    user.setVerified(true);
    userRepository.save(user);
    return "Verified.";
  }

  @Override
  public String verifyListing(Long id) {
    ListingEntity listing = listingRepository.findById(id).orElseThrow(()->new NotFoundException("Listing Not Found"));
    listing.setVerification_status("VERIFIED");
    listingRepository.save(listing);
    return "Verified.";  }

  @Override
  public List<Optional<UserEntity>> getUsers(JsonNode jsonNode) {
	  if (jsonNode.has("area")) {
	  	  String area = jsonNode.get("area") != null ? jsonNode.get("area").asText() : null;
	  	  if (area == null || area.isEmpty() || area.equals("null")) {
	  	   	  return userRepository.findByVerificationStatus();
	  	  }
	      return userRepository.findByArea(area);
	  }
   	  return Collections.emptyList();
  }

  @Override
  public List<ListingEntity> getListings(JsonNode jsonNode) {
	  if (jsonNode.has("area")) {
	  	  String area = jsonNode.get("area") != null ? jsonNode.get("area").asText() : null;
	  	  if (area == null || area.isEmpty() || area.equals("null")) {
	  		  return listingRepository.findAll();
	  	  }
	  	  return listingRepository.findByArea(area);
	  }
	  return Collections.emptyList();
  }

    @Override
	public List<AgentEntity> getAllAgents(JsonNode jsonNode) {
		if (jsonNode != null) {
			String office = jsonNode.get("office") != null ? jsonNode.get("office").asText() : null;
			String area = jsonNode.get("area") != null ? jsonNode.get("area").asText() : null;
			boolean officeGiven = office != null && !office.isEmpty();
			boolean areaGiven = area != null && !area.isEmpty();
			if (officeGiven || areaGiven) {
				if (officeGiven && areaGiven) {
					if (area.equals("assigned")) {
						List<AgentEntity> agents = agentRepository.findByOfficeAndAreaAssigned(office);
						populateAreasOfAgents(agents);
						return agents;
					} else if (area.equals("unassigned")) {
						List<AgentEntity> agents = agentRepository.findByOfficeAndAreaUnAssigned(office);
				    	for (AgentEntity agent : agents) {
						    agent.setArea("");
						}
						return agents;
					}
					Optional<AgentEntity> agentOptional = agentRepository.findByOfficeAndArea(office, area);
					return agentOptional.map(agent -> {
					    populateAreasOfAgents(List.of(agent));
					    return List.of(agent);
					}).orElse(Collections.emptyList());
				} else if (officeGiven) {
					List<AgentEntity> agents = agentRepository.findByOffice(office);
					populateAreasOfAgents(agents);
					return agents;
				} else {
					if (area.equals("assigned")) {
						List<AgentEntity> agents = agentRepository.findByAreaAssigned();
						populateAreasOfAgents(agents);
						return agents;
					} else if (area.equals("unassigned")) {
						List<AgentEntity> agents = agentRepository.findByAreaUnAssigned();
				    	for (AgentEntity agent : agents) {
						    agent.setArea("");
						}
						return agents;
					}
					Optional<AgentEntity> agentOptional = agentRepository.findByArea(area);
					return agentOptional.map(agent -> {
					    populateAreasOfAgents(List.of(agent));
					    return List.of(agent);
					}).orElse(Collections.emptyList());
				}
			}
		}
		List<AgentEntity> agents = agentRepository.findAll();
		populateAreasOfAgents(agents);
		return agents;
	}
    
    private void populateAreasOfAgents(List<AgentEntity> agents) {
    	for (AgentEntity agent : agents) {
		    String area = "";
		    for (AreaEntity areaEntity : areaRepository.findByAgentId(agent.getId())){
		      if (area.isEmpty()) area+=areaEntity.getArea();
		      else area += ", "+areaEntity.getArea();
		    }
		    agent.setArea(area);
		}
    }

  @Override
  public List<ListingEntity> getOlderListings(JsonNode jsonNode) {
	  if (jsonNode.has("area")) {
		  LocalDateTime date = LocalDateTime.now().minusDays(60);
	  	  String area = jsonNode.get("area") != null ? jsonNode.get("area").asText() : null;
	  	  if (area == null || area.isEmpty() || area.equals("null")) {
	  		  return listingRepository.findByDate(date);
	  	  }
	  	  return listingRepository.findByAreaAndDate(area, date);
	  }
	  return Collections.emptyList();
  }

  @Override
  public String hideListing(Long id) {
    ListingEntity listing = listingRepository.findById(id).orElseThrow(()->new NotFoundException("Listing Not Found"));
    listing.setVisible(0);
    listingRepository.save(listing);
    return "done.";
  }

  @Override
  public List<ListingEntity> getVListings(JsonNode jsonNode) {
    return listingRepository.findByAreaV(jsonNode.get("area").asText());
  }



  @Override
  public Optional<AgentEntity> getAgentById(Long id) {
    AgentEntity agentEntity = agentRepository.findById(id).get();
    String area = "";
    for (AreaEntity areaEntity : areaRepository.findByAgentId(agentEntity.getId())){
      if (area.isEmpty()) area+=areaEntity.getArea();
      else area += ", "+areaEntity.getArea();
    }
    agentEntity.setArea(area);
    return Optional.of(agentEntity);
  }


  @Override
  public JsonNode getAreasMapping() {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    for (AreaEntity e : areaRepository.findAll()){
        node.put(e.getArea(), String.valueOf(e.getAgentEntity() != null ? e.getAgentEntity().getId() : null));
    }
    return node;
  }

  @Override
  public List<String> getAreas() {
    List<String> areas = new ArrayList<>();
    for (AreaEntity e : areaRepository.findAll()){
      areas.add(e.getArea());
    }
    areas.remove(null);
    return areas;
  }
  
  @Override
  public JsonNode submitAgentTaskForm(AgentTaskFormEntity agentTaskFormEntity) throws ParseException {
	  ObjectNode jsonNodeResp = JsonNodeFactory.instance.objectNode();
	  Long agentId = agentTaskFormEntity.getAgentEntity() != null ? agentTaskFormEntity.getAgentEntity().getId() : null;
	  String dateString = agentTaskFormEntity.getDate();
	  String area = agentTaskFormEntity.getArea();
	  if (agentId == null) jsonNodeResp.put("error", "Mandatory info [agentEntity.id] missing");
	  else if (area == null) jsonNodeResp.put("error", "Mandatory info [area] missing");
	  else if (dateString == null) jsonNodeResp.put("error", "Mandatory info [date] missing");
	  else {
		  AgentEntity agent = agentRepository.findById(agentId).orElse(null);
		  if (agent == null) jsonNodeResp.put("error", "Agent with agentId '" + agentId + "' does not exist");
		  else {
			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			  Date date = dateFormat.parse(dateString);
              String formattedDate = dateFormat.format(date);
              if (!formattedDate.equals(dateString)) jsonNodeResp.put("error", "Invalid date format. Expected format: yyyy-MM-dd");
              else {
			      AgentTaskFormEntity existingAgentTaskForm = agentTaskFormRepository.findByAgentAreaAndDate(agentId, area, dateString);
			      if (existingAgentTaskForm != null) jsonNodeResp.put("error", "You have already submitted the Agent Task Form for area '" + area + "' today!");
			      else {
			    	  List<ClientReportEntity> clientReports = agentTaskFormEntity.getClientReports();
			    	  agentTaskFormEntity.setClientReports(null);
			    	  // Save the AgentTaskFormEntity to generate the task_form_id
			    	  agentTaskFormEntity = agentTaskFormRepository.save(agentTaskFormEntity);
			    	  if (clientReports != null) {
			    		  // Set the task_form_id in each ClientReportEntity
			    		  for (ClientReportEntity clientReport : clientReports) {
					         clientReport.setAgentTaskFormEntity(agentTaskFormEntity);
					         clientReportRepository.save(clientReport);
			    		  }
			    		  agentTaskFormEntity.setClientReports(clientReports);
			    		  agentTaskFormEntity = agentTaskFormRepository.save(agentTaskFormEntity);
			    	  }
			    	  ObjectMapper objectMapper = new ObjectMapper();
			    	  jsonNodeResp.set("agentTaskFormEntity", objectMapper.valueToTree(agentTaskFormEntity));
		          }
              }
		  }
	  }
	  return jsonNodeResp;  
  }
  
}