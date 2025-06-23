package com.squarenine.squarenine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.FormsSellEntity;

@Repository
public interface FormsSellRepository extends JpaRepository<FormsSellEntity, Long> {
  List<FormsSellEntity> findAllByArea(String area);
}
