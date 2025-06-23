package com.squarenine.squarenine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
