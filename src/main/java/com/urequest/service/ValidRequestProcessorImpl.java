package com.urequest.service;

import com.urequest.dto.ValidatedRequest;
import com.urequest.service.interfaces.RedisService;
import com.urequest.service.interfaces.ValidRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidRequestProcessorImpl implements ValidRequestProcessor {

    private final RedisService redisService;

    @Value("${urequest.redis.updated-count-set:requests-changed-keys-set}")
    private String updatedCountSetKey;

    @Autowired
    public ValidRequestProcessorImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void process(ValidatedRequest validatedRequest) {

    }

    @Override
    public void emitRequestValidatedEvent(ValidatedRequest validatedRequest) {
        String changedKey = validatedRequest.toKey();
        redisService.incr(changedKey);
        redisService.sadd(updatedCountSetKey, changedKey);
    }
}
