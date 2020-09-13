package com.urequest.repository;

import com.urequest.domain.Customer;
import com.urequest.domain.HourlyStats;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HourlyStatsRepository extends CrudRepository<HourlyStats, Integer> {
    List<HourlyStats> findAllByCustomerIdAndDate(Integer customerId, LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<HourlyStats> findByDateAndTimeAndCustomerIsNull(LocalDate toLocalDate, LocalTime toLocalTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<HourlyStats> findByCustomerAndDateAndTime(Customer customer, LocalDate date, LocalTime time);

    default Optional<HourlyStats> findByOptionalCustomerAndDateAndTime(Optional<Customer> customer, LocalDate date, LocalTime time) {
        if (customer.isPresent()) {
            return findByCustomerAndDateAndTime(customer.get(), date, time);
        }
        else {
            return findByDateAndTimeAndCustomerIsNull(date, time);
        }
    }
}

