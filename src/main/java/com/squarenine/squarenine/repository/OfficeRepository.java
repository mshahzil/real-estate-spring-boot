package com.squarenine.squarenine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.OfficeEntity;

@Repository
public interface OfficeRepository extends JpaRepository<OfficeEntity, Long> {
}
