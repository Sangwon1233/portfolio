package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.domain.entity.VisitorLog;
import com.sangwon97.portfolio.repository.VisitorLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VisitorServiceImpl implements VisitorService {

    private final VisitorLogRepository visitorLogRepository;

    public VisitorServiceImpl(VisitorLogRepository visitorLogRepository) {
        this.visitorLogRepository = visitorLogRepository;
    }

    @Override
    public void logVisit(String ipAddress) {
        VisitorLog log = VisitorLog.builder()
                .ipAddress(ipAddress)
                .visitDate(LocalDate.now())
                .build();
        visitorLogRepository.save(log);
    }

    @Override
    public long countTodayVisits() {
        return visitorLogRepository.countByVisitDate(LocalDate.now());
    }


}
