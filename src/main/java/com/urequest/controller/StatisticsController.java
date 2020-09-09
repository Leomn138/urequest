package com.urequest.controller;

import com.urequest.dto.StatisticsResponseV1;
import com.urequest.service.interfaces.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RestController
public class StatisticsController {

    private final
    StatisticsService statisticsService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @RequestMapping(path = "/v1/statistics", method = RequestMethod.GET)
    public ResponseEntity<StatisticsResponseV1> getDailyBasisStatistic(@NotNull Integer customerId, @DateTimeFormat(pattern="yyyy-MM-dd")@NotNull LocalDate date) {
        try {
            StatisticsResponseV1 response = statisticsService.getStatistics(customerId, date);
            return new ResponseEntity<>(response, response.getStatus());
        } catch(Exception e) {
            log.error("Error processing statistics for customer: " + customerId + " at date " + date + ".", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
