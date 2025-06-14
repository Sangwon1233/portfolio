package com.sangwon97.portfolio.service;

public interface VisitorService {
    void logVisit(String ipAddress);
    long countTodayVisits();
}