package com.urequest.service.interfaces;

public interface KafkaProducerService<T> {
    void send(String topic, String key, T event);
}
