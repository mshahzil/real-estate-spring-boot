package com.squarenine.squarenine.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.entity.FormsInvestEntity;
import com.squarenine.squarenine.entity.FormsSellEntity;
import com.squarenine.squarenine.service.FormsService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class FormsController {
    private final FormsService service;

    public FormsController(FormsService service) {
        this.service = service;
    }

    @GetMapping("/forms/invest/get/{id}")
    private Optional<FormsInvestEntity> getFormInvest(@PathVariable Long id){
        return service.getFormInvest(id);
    }

    @PostMapping("/forms/invest/get/all")
    private List<FormsInvestEntity> getFormsInvest(@RequestBody(required = false) JsonNode jsonNode){
        if (jsonNode != null && !jsonNode.get("area").isNull() && !jsonNode.get("area").asText().isEmpty()){
            return service.getFormsInvest(jsonNode.get("area").asText());
        }
        return service.getFormsInvest();
    }

    @GetMapping("/forms/sell/get/{id}")
    private Optional<FormsSellEntity> getFormSell(@PathVariable Long id){
        return service.getFormSell(id);
    }

    @PostMapping("/forms/sell/get/all")
    private List<FormsSellEntity> getFormsSell(@RequestBody(required = false) JsonNode jsonNode){
        if (jsonNode != null && !jsonNode.get("area").isNull() && !jsonNode.get("area").asText().isEmpty()){
            return service.getFormsSell(jsonNode.get("area").asText());
        }
        return service.getFormsSell();
    }

    @PostMapping("/forms/sell/add")
    private FormsSellEntity addFormSell(@RequestBody FormsSellEntity forumsEntity){
        return service.addFormSell(forumsEntity);
    }

    @PostMapping("/forms/invest/add")
    private FormsInvestEntity addFormInvest(@RequestBody FormsInvestEntity forumsEntity){
        return service.addFormInvest(forumsEntity);
    }
}
