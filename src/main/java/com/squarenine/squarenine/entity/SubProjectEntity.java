package com.squarenine.squarenine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sub_project")
public class SubProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long project_id;
    private String title;
    private String subtitle;
    private String description;
    private float rate;
    private String annualPercent;
    private String images;
    private String amenities;
}
