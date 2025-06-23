package com.squarenine.squarenine.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.dtos.ListingAlertPayload;
import com.squarenine.squarenine.entity.AlertEntity;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.repository.AlertRepository;
import com.squarenine.squarenine.repository.ListingRepository;
import com.squarenine.squarenine.repository.UserRepository;
import com.squarenine.squarenine.service.AlertService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository repository;
  private final UserRepository userRepository;
  private final ListingRepository listingRepository;

    @Override
    public String setAlert(ListingAlertPayload payload) {
        AlertEntity alert = AlertEntity
            .builder()
            .address(payload.getAddress())
            .baths(payload.getBaths())
            .area_marla(payload.getArea_marla())
            .price(payload.getPrice())
            .createdById(userRepository.findByUid(payload.getUid()).get().getId())
            .purpose(payload.getPurpose())
            .type(payload.getType())
            .beds(payload.getBeds())
            .build();
        repository.save(alert);
        return "Alert Set.";
    }

  @Override
  public List<AlertEntity> getAlerts(JsonNode jsonNode) {
    Long user_id = userRepository.findByUid(jsonNode.get("uid").asText()).get().getId();
    return repository.findByCb(user_id);
  }

  @Override
  public List<ListingEntity> getListings(JsonNode jsonNode) {
      List<ListingEntity> matching = new ArrayList<>();
    Long user_id = userRepository.findByUid(jsonNode.get("uid").asText()).get().getId();
    List<AlertEntity> alerts = repository.findByCb(user_id);
    for (AlertEntity a: alerts) {
      matching.addAll(listingRepository.findByAreaTypePrice(a.getArea_marla(),a.getType(),a.getPrice()));
    }
    return matching;
  }
}
