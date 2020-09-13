package com.urequest.unit.service;

import com.urequest.dto.ValidatedRequest;
import com.urequest.service.ValidRequestProcessorImpl;
import com.urequest.service.interfaces.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@SpringBootTest(classes= ValidRequestProcessorImpl.class)
public class ValidRequestProcessorServiceImplTests {

    @Autowired
    ApplicationContext context;

    @Autowired
    private ValidRequestProcessorImpl validRequestProcessor;

    @MockBean
    private RedisService redisService;

    @Test
    void  emitRequestValidatedEvent_calls_correct_methods() {
        ValidatedRequest validatedRequest = new ValidatedRequest(LocalDateTime.now(), true);
        String updatedSetKey = "requests-changed-keys-set";

        validRequestProcessor.emitRequestValidatedEvent(validatedRequest);

        verify(redisService, times(1)).sadd(updatedSetKey, validatedRequest.toKey());
        verify(redisService, times(1)).incr(validatedRequest.toKey());
    }

}
