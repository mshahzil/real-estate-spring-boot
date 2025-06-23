package com.squarenine.squarenine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Data
@Table(name = "person")
public class PersonEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String uid;
  private String firstname;
  private String lastname;
  private String username;
  @Column(unique=true)
  private String email;
  private String phone;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private boolean verified;
  private String verification_status;
  private String city;
  private String mobAppVersion;
  @JsonIgnore
  private String likedListings;
  private String documentImages;
  private boolean notificationsEnabled;
  private String area;
  @Transient
  private String role;
}
