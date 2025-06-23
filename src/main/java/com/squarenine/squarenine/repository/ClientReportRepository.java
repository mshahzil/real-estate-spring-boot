package com.squarenine.squarenine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.ClientReportEntity;

@Repository
public interface ClientReportRepository extends JpaRepository<ClientReportEntity, Long> {
}