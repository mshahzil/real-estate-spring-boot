package com.squarenine.squarenine.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.dtos.ListingAlertPayload;
import com.squarenine.squarenine.entity.AlertEntity;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.service.AlertService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping(value = "/alert/set")
    private String setAlert(@RequestBody ListingAlertPayload payload) {
        return alertService.setAlert(payload);
    }

    @PostMapping(value = "/alert/get/placed/byUid")
    private List<AlertEntity> getAlerts(@RequestBody JsonNode jsonNode) {
        return alertService.getAlerts(jsonNode);
    }

    @PostMapping(value = "/alert/get/matched/byUid")
    private List<ListingEntity> getListings(@RequestBody JsonNode jsonNode) {
        return alertService.getListings(jsonNode);
    }
}
