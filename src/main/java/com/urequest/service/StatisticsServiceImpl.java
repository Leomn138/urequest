package com.urequest.service;

import com.urequest.domain.HourlyStats;
import com.urequest.dto.StatisticsResponseV1;
import com.urequest.repository.HourlyStatsRepository;
import com.urequest.service.interfaces.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final HourlyStatsRepository hourlyStatsRepository;

    @Autowired
    public StatisticsServiceImpl(HourlyStatsRepository hourlyStatsRepository) {
        this.hourlyStatsRepository = hourlyStatsRepository;
    }

    @Override
    public StatisticsResponseV1 getStatistics(Integer customerId, LocalDate date) {
        StatisticsResponseV1 response = new StatisticsResponseV1();
        Long numberOfNonConsolidatedValidRequests = 0L;
        Long numberOfNonConsolidatedInvalidRequests = 0L;

        List<HourlyStats> consolidatedHourlyStats = hourlyStatsRepository.findAllByCustomerIdAndDate(customerId, date);
        if (date.compareTo(LocalDate.now()) >= 0) {
            response.setConsolidated(false);
            // TODO: get non consolidated data
        }
        else {
            response.setConsolidated(true);
        }

        Long numberOfConsolidatedValidRequests = consolidatedHourlyStats.stream()
            .map(HourlyStats::getRequestCount)
            .reduce(0L, Long::sum);

        Long numberOfConsolidatedInvalidRequests = consolidatedHourlyStats.stream()
            .map(HourlyStats::getInvalidCount)
            .reduce(0L, Long::sum);

        Long totalNumberOfValidRequests = numberOfConsolidatedValidRequests + numberOfNonConsolidatedValidRequests;
        Long totalNumberOfInvalidRequests = numberOfConsolidatedInvalidRequests + numberOfNonConsolidatedInvalidRequests;
        response.setNumberOfInvalidRequests(totalNumberOfInvalidRequests);
        response.setNumberOfValidRequests(totalNumberOfValidRequests);
        response.setTotalNumberOfRequests(totalNumberOfValidRequests + totalNumberOfInvalidRequests);
        response.setStatus(HttpStatus.OK);

        return response;
    }
}
