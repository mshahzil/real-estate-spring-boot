package com.squarenine.squarenine.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.dtos.ListingAlertPayload;
import com.squarenine.squarenine.entity.AlertEntity;
import com.squarenine.squarenine.entity.ListingEntity;

public interface AlertService {
  String setAlert(ListingAlertPayload payload);

  List<AlertEntity> getAlerts(JsonNode uid);

  List<ListingEntity> getListings(JsonNode jsonNode);
}
