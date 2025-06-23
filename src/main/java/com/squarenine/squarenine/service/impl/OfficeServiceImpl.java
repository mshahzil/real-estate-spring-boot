package com.squarenine.squarenine.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.squarenine.squarenine.entity.OfficeEntity;
import com.squarenine.squarenine.repository.OfficeRepository;
import com.squarenine.squarenine.service.OfficeService;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository repository;

    public OfficeServiceImpl(OfficeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<OfficeEntity> getOffice(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<OfficeEntity> getOffices() {
        return repository.findAll();
    }

    @Override
    public OfficeEntity addOffice(OfficeEntity officeEntity) {
      return repository.save(officeEntity);
    }
}
