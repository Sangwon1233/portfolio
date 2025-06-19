package com.sangwon97.portfolio.repository;

import com.sangwon97.portfolio.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
