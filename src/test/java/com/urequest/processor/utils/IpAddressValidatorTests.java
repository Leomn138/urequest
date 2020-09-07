package com.urequest.processor.utils;

import com.urequest.requestprocessor.utils.IpAddressValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes=IpAddressValidator.class)
public class IpAddressValidatorTests {

    @Test
    public void isValid_returnsTrue_whenIpIsValid() {
        List<String> validIps = Arrays.asList(
                "0.0.0.0", "10.0.0.0", "100.64.0.0", "127.0.0.0", "169.254.0.0", "172.16.0.0", "192.0.0.0", "192.0.0.0",
                "192.0.0.8", "192.0.0.9", "192.0.0.170", "192.0.0.171", "192.0.2.0", "192.31.196.0", "192.52.193.0",
                "192.88.99.0", "192.168.0.0", "192.175.48.0", "198.18.0.0", "198.51.100.0", "203.0.113.0", "240.0.0.0", "255.255.255.255"
        );

        validIps.forEach(ip -> assertTrue(IpAddressValidator.isValid(ip)));
    }

    @Test
    public void isValid_returnsFalse_whenIpIsNotValid() {
        List<String> invalidIps = Arrays.asList("", "    ", "1234", "DOES.NOT.EX.IST", "1.2.3", "1.2.3.", "1.2.3.0.1");

        invalidIps.forEach(ip -> assertFalse(IpAddressValidator.isValid(ip)));
    }
}
