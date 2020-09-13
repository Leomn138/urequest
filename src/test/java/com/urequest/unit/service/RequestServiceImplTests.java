package com.urequest.unit.service;

import com.urequest.domain.Customer;
import com.urequest.domain.Ip;
import com.urequest.domain.UserAgent;
import com.urequest.dto.ProcessResponseV1;
import com.urequest.repository.CustomerRepository;
import com.urequest.repository.IpBlacklistRepository;
import com.urequest.repository.UserAgentBlacklistRepository;
import com.urequest.service.RequestServiceImpl;
import com.urequest.service.interfaces.ValidRequestProcessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes=RequestServiceImpl.class)
public class RequestServiceImplTests {

    @Autowired
    private RequestServiceImpl requestService;

    @MockBean
    private UserAgentBlacklistRepository userAgentBlacklistRepository;

    @MockBean
    private IpBlacklistRepository ipBlacklistRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private ValidRequestProcessor validRequestProcessor;

    @Autowired
    ApplicationContext context;

    @Test
    void process_happy_path_returns_ok() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIp\":\"123.234.56.78\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";
        Customer customer = new Customer();
        customer.setActive(true);

        when(userAgentBlacklistRepository.findByUserAgent(any())).thenReturn(Optional.empty());
        when(ipBlacklistRepository.findByIp(any())).thenReturn(Optional.empty());
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        verify(validRequestProcessor, times(1)).process(any());
        assertEquals(response.getStatus(), HttpStatus.OK);
    }

    @Test
    void process_returns_bad_request_when_customer_is_missing_in_the_request() {
        final String request = "{\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIp\":\"123.234.56.78\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_tag_is_missing_in_the_request() {
        final String request = "{\"customerId\":1,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIp\":\"123.234.56.78\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_user_is_missing_in_the_request() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"remoteIp\":\"123.234.56.78\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_remoteip_is_missing_in_the_request() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_timestamp_is_missing_in_the_request() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIp\":\"123.234.56.78\"}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_json_is_malformed() {
        final String request = "{malformed}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_ip_address_is_blacklisted() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"21.307.064.33\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";

        Ip ip = new Ip();
        ip.setIp("ip");

        ProcessResponseV1 response = requestService.process(userAgent, request);

        when(ipBlacklistRepository.findByIp(ip.getIp())).thenReturn(Optional.of(ip));

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_user_agent_is_blacklisted() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"21.307.064.33\",\"timestamp\":1500000000}";

        UserAgent userAgent = new UserAgent();
        userAgent.setUserAgent("userAgent");

        ProcessResponseV1 response = requestService.process(userAgent.getUserAgent(), request);

        when(ipBlacklistRepository.findByIp(any())).thenReturn(Optional.empty());
        when(userAgentBlacklistRepository.findByUserAgent(userAgent.getUserAgent())).thenReturn(Optional.of(userAgent));

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void process_returns_bad_request_when_customer_does_not_exist() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"21.307.064.33\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";

        ProcessResponseV1 response = requestService.process(userAgent, request);

        when(ipBlacklistRepository.findByIp(any())).thenReturn(Optional.empty());
        when(userAgentBlacklistRepository.findByUserAgent(userAgent)).thenReturn(Optional.empty());
        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }
    @Test
    void process_returns_bad_request_when_customer_is_not_active() {
        final String request = "{\"customerId\":1,\"tagId\":2,\"userId\":\"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\":\"21.307.064.33\",\"timestamp\":1500000000}";
        final String userAgent = "userAgent";
        Customer customer = new Customer();
        customer.setId(1);
        customer.setActive(false);

        ProcessResponseV1 response = requestService.process(userAgent, request);

        when(ipBlacklistRepository.findByIp(any())).thenReturn(Optional.empty());
        when(userAgentBlacklistRepository.findByUserAgent(userAgent)).thenReturn(Optional.empty());
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        verify(validRequestProcessor, times(1)).emitRequestValidatedEvent(any());
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

}
