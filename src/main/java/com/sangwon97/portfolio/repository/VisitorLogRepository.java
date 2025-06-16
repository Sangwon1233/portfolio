package com.sangwon97.portfolio.repository;

import com.sangwon97.portfolio.domain.entity.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
    long countByVisitDate(LocalDate visitDate);
}
