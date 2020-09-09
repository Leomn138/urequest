package com.urequest.repository;

import com.urequest.domain.HourlyStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HourlyStatsRepository extends CrudRepository<HourlyStats, Integer> {
    List<HourlyStats> findAllByCustomerIdAndDate(Integer customerId, LocalDate date);
}

