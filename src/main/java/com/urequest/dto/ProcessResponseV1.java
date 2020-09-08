package com.urequest.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProcessResponseV1 {
    private HttpStatus status;

    public ProcessResponseV1(HttpStatus status) {
        this.status = status;
    }

    public ProcessResponseV1() {

    }
}
