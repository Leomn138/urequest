package com.urequest.service;

import com.urequest.service.interfaces.RedisService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisServiceImpl implements RedisService {

    @Override
    public void sadd(String key, String value) {
        getResource().sadd(key, value);
    }

    @Override
    public String spop(String key) { return getResource().spop(key); }

    @Override
    public void incr(String key) {
        getResource().incr(key);
    }

    @Override
    public String get(String key) {
        return getResource().get(key);
    }

    private Jedis getResource() {
        return new Jedis();
    }
}
