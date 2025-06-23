package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import com.squarenine.squarenine.entity.OfficeEntity;

public interface OfficeService {
    Optional<OfficeEntity> getOffice(Long id);

    List<OfficeEntity> getOffices();

    OfficeEntity addOffice(OfficeEntity officeEntity);
}
