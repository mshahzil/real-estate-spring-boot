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

import com.squarenine.squarenine.entity.OfficeEntity;
import com.squarenine.squarenine.service.OfficeService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class OfficeController {
    private final OfficeService service;

    public OfficeController(OfficeService service) {
        this.service = service;
    }

    @GetMapping("/office/get/{id}")
    private Optional<OfficeEntity> getListing(@PathVariable Long id){
        return service.getOffice(id);
    }

    @GetMapping("/office/get/all")
    private List<OfficeEntity> getListings(){
        return service.getOffices();
    }

    @PostMapping("/office/add")
    private OfficeEntity addOffice(@RequestBody OfficeEntity officeEntity){
        return service.addOffice(officeEntity);
    }
}
