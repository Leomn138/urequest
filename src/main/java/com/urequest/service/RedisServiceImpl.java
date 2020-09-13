package com.urequest.service;

import com.urequest.service.interfaces.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class RedisServiceImpl implements RedisService {

    private JedisPool jedisPool;

    private String host;
    private Integer port;
    private Integer timeout;

    @Autowired
    public RedisServiceImpl(@Value("${urequest.redis.host}") String host,
                            @Value("${urequest.redis.port}") Integer port,
                            @Value("${urequest.redis.timeout}") Integer timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

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
        return jedisPool.getResource();
    }

    private JedisPoolConfig buildPoolConfig() {
        return new JedisPoolConfig();
    }

    @PostConstruct
    private void init() {
        jedisPool = new JedisPool(buildPoolConfig(), host, port, timeout);
    }

    @PreDestroy
    private void destroy() {
        if (jedisPool != null) {
            jedisPool.destroy();
        }
    }
}
