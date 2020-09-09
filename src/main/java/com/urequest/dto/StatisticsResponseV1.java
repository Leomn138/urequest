package com.urequest.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class StatisticsResponseV1 {
    private Long numberOfValidRequests;
    private Long numberOfInvalidRequests;
    private Long totalNumberOfRequests;
    private boolean consolidated;
    private HttpStatus status;
}
