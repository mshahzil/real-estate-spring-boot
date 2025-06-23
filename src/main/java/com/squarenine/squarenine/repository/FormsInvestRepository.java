package com.squarenine.squarenine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.FormsInvestEntity;

@Repository
public interface FormsInvestRepository extends JpaRepository<FormsInvestEntity, Long> {
  List<FormsInvestEntity> findAllByArea(String area);

}
