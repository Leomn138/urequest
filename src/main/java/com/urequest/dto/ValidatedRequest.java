package com.urequest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.util.Optional;

@Getter
@Setter
public class ValidatedRequest {

    private Integer customerId;
    private boolean isValid;
    private LocalDateTime datetime;

    public ValidatedRequest(LocalDateTime datetime, boolean isValid) {
        this.datetime = datetime;
        this.isValid = isValid;
    }

    public ValidatedRequest(Integer customerId, LocalDateTime datetime, boolean isValid) {
        this.customerId = customerId;
        this.datetime = datetime;
        this.isValid = isValid;
    }

    public static ValidatedRequest of(Optional<ProcessRequest> processRequest, boolean isValid) {
        if (processRequest.isPresent()) {
            if (processRequest.get().getTimestamp() != null) {
                ZonedDateTime dateTime = Instant.ofEpochMilli(processRequest.get().getTimestamp()).atZone(ZoneId.systemDefault());
                return new ValidatedRequest(processRequest.get().getCustomerId(), dateTime.toLocalDateTime(), isValid);
            }
            else {
                return new ValidatedRequest(processRequest.get().getCustomerId(), LocalDateTime.now(), isValid);
            }
        }
        return new ValidatedRequest(LocalDateTime.now(), isValid);
    }

    public static ValidatedRequest fromKey(String key) {
        String[] keys = key.split("-");
        String year = keys[0];
        String month = keys[1];
        String day = keys[2];
        String hour = keys[3];
        String isValid = keys[4];
        String customerId = keys[5];

        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)), LocalTime.of(Integer.parseInt(hour), 0));
        if (customerId != null && !customerId.isEmpty()) {
            return new ValidatedRequest(Integer.parseInt(customerId), localDateTime, Boolean.parseBoolean(isValid));
        } else {
            return new ValidatedRequest(localDateTime, Boolean.parseBoolean(isValid));
        }
    }

    public String toKey() {
        String customerIdStr = "";
        if (customerId != null) {
            customerIdStr = customerId.toString();
        }
        return datetime.getYear() + "-" + datetime.getMonthValue() + "-" + datetime.getDayOfMonth() + "-" + datetime.getHour() + "-" + isValid + "-" + customerIdStr;
    }
}
