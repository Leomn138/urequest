package com.urequest.service.interfaces;

import com.urequest.dto.StatisticsResponseV1;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

public interface StatisticsService {
    StatisticsResponseV1 getStatistics(Integer customerId, LocalDate localDate);

    @Scheduled(cron = "${urequest.statistics.consolidated.cron}")
    void refreshConsolidatedData();
}
