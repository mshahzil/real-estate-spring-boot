package com.squarenine.squarenine.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue("agent")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class AgentEntity extends PersonEntity{
  private String office;
  private String ranking;
  private String designation;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "agentEntity")
  @JsonIgnore
  private List<AreaEntity> areaEntities;
}
