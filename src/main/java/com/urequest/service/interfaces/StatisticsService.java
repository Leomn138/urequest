package com.urequest.service.interfaces;

import com.urequest.dto.StatisticsResponseV1;

import java.time.LocalDate;

public interface StatisticsService {
    StatisticsResponseV1 getStatistics(Integer customerId, LocalDate localDate);
}
