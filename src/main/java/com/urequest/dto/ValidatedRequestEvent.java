package com.urequest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatedRequestEvent {
    private Long timestamp;
    private boolean isValid;

    public ValidatedRequestEvent(Long timestamp, boolean isValid) {
        this.timestamp = timestamp;
        this.isValid = isValid;
    }
}
