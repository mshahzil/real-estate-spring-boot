package com.squarenine.squarenine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.squarenine.squarenine.entity.NewsEntity;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
}
