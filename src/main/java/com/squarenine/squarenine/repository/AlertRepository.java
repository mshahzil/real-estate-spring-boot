package com.squarenine.squarenine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.AlertEntity;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    @Query("Select c from AlertEntity c where c.address like CONCAT('%',:address,'%')")
    List<AlertEntity> findByAddressLike(String address);

    @Query("Select c from AlertEntity c where c.area_marla = :area AND c.type = :type")
    List<AlertEntity> findByAreaLike(float area, String type);

    @Query("Select c from AlertEntity c where c.createdById = :cb")
    List<AlertEntity> findByCb(Long cb);
}
