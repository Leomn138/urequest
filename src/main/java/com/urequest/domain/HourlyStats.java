package com.urequest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "hourly_stats")
public class HourlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    private LocalTime time;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long requestCount;

    @NotNull
    private Long invalidCount;

    public HourlyStats() {}

    public HourlyStats(Customer customer, LocalDate date, LocalTime time, Long validRequestsCount, Long invalidRequestsCount) {
        this.customer = customer;
        this.date = date;
        this.time = time;
        this.invalidCount = invalidRequestsCount;
        this.requestCount = validRequestsCount;
    }

    public HourlyStats(Optional<Customer> customer, LocalDateTime datetime, boolean isValid, Long requestsCount) {
        customer.ifPresent(value -> this.customer = value);
        this.date = datetime.toLocalDate();
        this.time = datetime.toLocalTime();
        setCount(isValid, requestsCount);
    }

    public void setCount(boolean isValid, long requestsCount) {
        if (isValid) {
            setRequestCount(requestsCount);
        } else {
           setInvalidCount(requestsCount);
        }
    }
}
