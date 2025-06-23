package com.squarenine.squarenine.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "listing")
public class ListingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type="";
    private String title="";
    private String purpose="";
    private long price;
    private String city="";
    private String address="";
    private String sector = "";
    private String sub_sector = "";
    private String street = "";
    private String house_no = "";
    private double latitude;
    private double longitude;
    private float area_marla;
    private String description = "";
    private int beds;
    private int baths;
    private LocalDateTime listed_date;
    private int featured;
    private String images;
    private String documents;
    @Transient
    private String createdBy = "";
    private long agent_id;
    private String verification_status;
    @Transient
    private boolean isLiked;
    private Long createdById;
    private String area ="";
    private int visible = 1;
}
