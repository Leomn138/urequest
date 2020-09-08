package com.urequest.dto;

import com.urequest.utils.IpAddressValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ProcessRequest {

    @NotNull
    private Integer customerId;
    @NotNull
    private Integer tagId;
    @NotNull
    private String userId;
    @NotNull
    @Pattern(regexp= IpAddressValidator.IP_REGEXP)
    private String remoteIp;
    @NotNull
    private Long timestamp;

    public String toString() {
        return "CustomerId: " + customerId +
        "\n tagId: " + tagId +
        "\n userId: " + userId +
        "\n remoteIp: " + remoteIp +
        "\n timestamp: " + timestamp;
    }
}
