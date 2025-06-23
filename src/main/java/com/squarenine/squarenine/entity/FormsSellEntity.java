package com.squarenine.squarenine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "forms_sell")
public class FormsSellEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String phone;
    private String type;
    private String location;
    private String size;
    private String features;
    private int beds;
    private int baths;
    @Transient
    private String uid;
    private Long user_id;
    private String area;
}
