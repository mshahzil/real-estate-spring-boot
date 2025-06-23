package com.squarenine.squarenine.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("regionalHead")
public class RegionalHeadEntity extends PersonEntity{}
