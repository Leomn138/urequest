package com.urequest.service.interfaces;

public interface RedisService {
    void sadd(String key, String value);

    String spop(String key);

    void incr(String key);

    String get(String key);
}
