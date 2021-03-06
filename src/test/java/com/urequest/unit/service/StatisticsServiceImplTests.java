package com.urequest.unit.service;

import com.urequest.domain.Customer;
import com.urequest.domain.HourlyStats;
import com.urequest.dto.StatisticsResponseV1;
import com.urequest.repository.CustomerRepository;
import com.urequest.repository.HourlyStatsRepository;
import com.urequest.service.StatisticsServiceImpl;
import com.urequest.service.interfaces.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes= StatisticsServiceImpl.class)
public class StatisticsServiceImplTests {

    @Autowired
    ApplicationContext context;

    @Autowired
    private StatisticsServiceImpl statisticsService;

    @MockBean
    private RedisService redisService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private HourlyStatsRepository hourlyStatsRepository;

    @Test
    void getStatistics_returns_zero_sums_when_nothing_is_found() {
        final Integer customerId = 1;
        final LocalDate date = LocalDate.of(2020, 5, 8);

        List<HourlyStats> hourlyStats = new ArrayList<>();

        when(hourlyStatsRepository.findAllByCustomerIdAndDate(customerId, date)).thenReturn(hourlyStats);

        StatisticsResponseV1 response = statisticsService.getStatistics(customerId, date);

        assertEquals(response.getNumberOfInvalidRequests(), 0);
        assertEquals(response.getNumberOfValidRequests(), 0);
        assertEquals(response.getTotalNumberOfRequests(), 0);
        assertEquals(response.getStatus(), HttpStatus.OK);
    }

    @Test
    void getStatistics_returns_consolidated_counts_when_stats_in_the_past_are_found() {
        final Integer customerId = 1;
        final LocalDate date = LocalDate.of(2020, 5, 8);
        final LocalTime time = LocalTime.now();
        Customer customer = new Customer();

        List<HourlyStats> hourlyStats = new ArrayList<>();

        hourlyStats.add(new HourlyStats(customer, date, time, 10L, 20L));
        hourlyStats.add(new HourlyStats(customer, date, time.plusHours(1), 15L, 15L));
        hourlyStats.add(new HourlyStats(customer, date, time.minusHours(1), 20L, 10L));

        when(hourlyStatsRepository.findAllByCustomerIdAndDate(customerId, date)).thenReturn(hourlyStats);

        StatisticsResponseV1 response = statisticsService.getStatistics(customerId, date);

        assertEquals(response.getNumberOfInvalidRequests(), 45);
        assertEquals(response.getNumberOfValidRequests(), 45);
        assertEquals(response.getTotalNumberOfRequests(), 90);
        assertEquals(response.getStatus(), HttpStatus.OK);
    }
    @Test
    void refreshConsolidatedData_does_nothing_when_there_is_nothing_to_process() {
        String updatedSetKey = "requests-changed-keys-set";

        statisticsService.refreshConsolidatedData();

        when(redisService.spop(updatedSetKey)).thenReturn(null);
        verify(redisService, times(1)).spop(updatedSetKey);
    }

    @Test
    void refreshConsolidatedData_gets_incremented_data_and_updates_valid_entries_correctly() {

    }

    @Test
    void refreshConsolidatedData_gets_incremented_data_and_updates_invalid_entries_correctly() {

    }

    @Test
    void refreshConsolidatedData_gets_incremented_data_and_creates_new_entry_on_relational_db() {

    }

    @Test
    void refreshConsolidatedData_keeps_running_when_an_entry_cannot_be_parsed() {

    }

    @Test
    void refreshConsolidatedData_gets_incremented_data_and_updates_on_relational_db_when_customer_is_missing() {

    }
}
