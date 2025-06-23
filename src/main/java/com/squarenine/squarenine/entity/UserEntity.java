package com.squarenine.squarenine.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("user")
@Data
public class UserEntity extends PersonEntity{}
