package com.squarenine.squarenine.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.ListingEntity;

@Repository
public interface ListingRepository extends JpaRepository<ListingEntity, Long> {
    @Query("Select c from ListingEntity c where c.visible = 1")
    List<ListingEntity> findAll();
    
    @Query("Select c from ListingEntity c where c.address like CONCAT('%',:address,'%') and c.visible = 1")
    List<ListingEntity> findByAddressLike(String address);

    @Query("Select c from ListingEntity c where c.area_marla = :area AND c.type = :type and c.visible = 1")
    List<ListingEntity> findByAreaLike(float area, String type);

    @Query("Select c from ListingEntity c where c.area_marla = :area AND c.type = :type AND c.price = :price and c.visible = 1")
    List<ListingEntity> findByAreaTypePrice(float area, String type, float price);
    
    @Query("Select c from ListingEntity c where c.createdById = :cb and c.visible = 1")
    List<ListingEntity> findByCb(Long cb);

    @Query("Select c from ListingEntity c where c.featured = 1 and c.visible = 1")
    List<ListingEntity> findByFeatured();
    
    @Query("Select c from ListingEntity c where c.agent_id = :id and c.visible = 1")
    List<ListingEntity> findByAgent(long id);

    @Query("Select c from ListingEntity c where c.area = :area and c.visible = 1")
    List<ListingEntity> findByArea(String area);
    
    @Query("Select c from ListingEntity c where c.area = :area and c.listed_date < :dateTime and c.visible = 1")
    List<ListingEntity> findByAreaAndDate(String area, LocalDateTime dateTime);

    @Query("Select c from ListingEntity c where c.area = :area and c.verification_status = 'PENDING' and c.visible = 1")
    List<ListingEntity> findByAreaV(String area);
    
    @Query("Select c from ListingEntity c where c.listed_date < :dateTime and c.visible = 1")
    List<ListingEntity> findByDate(LocalDateTime dateTime);
}
