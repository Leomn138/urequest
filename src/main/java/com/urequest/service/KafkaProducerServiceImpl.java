package com.urequest.service;

import com.urequest.service.interfaces.KafkaProducerService;
import com.urequest.utils.GsonSerializer;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
class KafkaProducerServiceImpl<T> implements Closeable, KafkaProducerService<T> {

    private String address;

    KafkaProducer<String, T> producer;

    @Autowired
    KafkaProducerServiceImpl(@Value("${urequest.kafka.address}") String address) {
        this.address = address;
        producer = new KafkaProducer<>(getDefaultProperties());
    }

    private Callback getDefaultCallbackFunction = (RecordMetadata recordMetadata, Exception e) -> {
        if (e != null) {
            e.printStackTrace();
        }
    };

    public void send(@NotEmpty String topic, @NotEmpty String key, @NotNull T value) {
        send(topic, key, value, getDefaultCallbackFunction);
    }

    private void send(@NotEmpty String topic, @NotEmpty String key, @NotNull T value, @NotNull Callback callbackFunction) {
        ProducerRecord<String, T> record = new ProducerRecord<>(topic, key, value);
        try {
            producer.send(record, callbackFunction).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Properties getDefaultProperties() {
        Properties properties = new Properties();
        String keySerializer = GsonSerializer.class.getName();
        String valueSerializer = GsonSerializer.class.getName();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return properties;
    }

    @Override
    public void close() {
        producer.close();
    }
}
