package com.urequest.service;

import com.urequest.domain.Customer;
import com.urequest.domain.HourlyStats;
import com.urequest.dto.StatisticsResponseV1;
import com.urequest.dto.ValidatedRequest;
import com.urequest.repository.CustomerRepository;
import com.urequest.repository.HourlyStatsRepository;
import com.urequest.service.interfaces.RedisService;
import com.urequest.service.interfaces.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final HourlyStatsRepository hourlyStatsRepository;

    private final RedisService redisService;

    private final CustomerRepository customerRepository;

    @Value("${urequest.redis.updated-count-set:requests-changed-keys-set}")
    private String updatedCountSetKey;

    @Autowired
    public StatisticsServiceImpl(HourlyStatsRepository hourlyStatsRepository, RedisService redisService, CustomerRepository customerRepository) {
        this.hourlyStatsRepository = hourlyStatsRepository;
        this.redisService = redisService;
        this.customerRepository = customerRepository;
    }

    @Override
    public StatisticsResponseV1 getStatistics(Integer customerId, LocalDate date) {
        List<HourlyStats> consolidatedHourlyStats = hourlyStatsRepository.findAllByCustomerIdAndDate(customerId, date);

        Long numberOfConsolidatedValidRequests = consolidatedHourlyStats.stream()
            .map(HourlyStats::getRequestCount)
            .reduce(0L, Long::sum);

        Long numberOfConsolidatedInvalidRequests = consolidatedHourlyStats.stream()
            .map(HourlyStats::getInvalidCount)
            .reduce(0L, Long::sum);

        StatisticsResponseV1 response = new StatisticsResponseV1();
        response.setNumberOfInvalidRequests(numberOfConsolidatedInvalidRequests);
        response.setNumberOfValidRequests(numberOfConsolidatedValidRequests);
        response.setTotalNumberOfRequests(numberOfConsolidatedInvalidRequests + numberOfConsolidatedValidRequests);
        response.setStatus(HttpStatus.OK);

        return response;
    }

    @Override
    @Scheduled(cron = "${urequest.statistics.consolidated.cron}")
    public void refreshConsolidatedData() {
        String incrementedKey = redisService.spop(updatedCountSetKey);
        while (incrementedKey != null) {
            String count = redisService.get(incrementedKey);
            long requestsCount = Long.parseLong(count) + 1;

            ValidatedRequest validatedRequest = ValidatedRequest.fromKey(incrementedKey);

            Optional<Customer> customer = customerRepository.findById(validatedRequest.getCustomerId());
            Optional<HourlyStats> savedHourlyStats = hourlyStatsRepository.findByOptionalCustomerAndDateAndTime(customer, validatedRequest.getDatetime().toLocalDate(), validatedRequest.getDatetime().toLocalTime());

            updatedRequestsCount(requestsCount, validatedRequest, customer, savedHourlyStats);
            incrementedKey = redisService.spop(updatedCountSetKey);
        }
    }

    private void updatedRequestsCount(long requestsCount, ValidatedRequest validatedRequest, Optional<Customer> customer, Optional<HourlyStats> savedHourlyStats) {
        HourlyStats hourlyStats;
        if (savedHourlyStats.isPresent()) {
            hourlyStats = savedHourlyStats.get();
            hourlyStats.setCount(validatedRequest.isValid(), requestsCount);
        } else {
            hourlyStats = new HourlyStats(customer, validatedRequest.getDatetime(), validatedRequest.isValid(), requestsCount);
        }
        hourlyStatsRepository.save(hourlyStats);
    }
}
