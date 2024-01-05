package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.commons.NewOrder;
import org.fedous.generated.AvroOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.IntStream;

@Service
@Slf4j
public class KafkaOrderSender {

    @Autowired
    KafkaTemplate<String, NewOrder> kafkaTemplate;
    @Autowired
    KafkaTemplate<Integer, AvroOrder> kafkaTemplateAvro;

    public void send(NewOrder order) {

        String kafkaTopic = "testing-order";
        kafkaTemplate.send(kafkaTopic, order.getCustomerName(), order);
        log.info("Sent: {}", order);
    }

    public void sendAvro(int key, AvroOrder order) {

        try {
            String kafkaTopic = "testing-order-avro";
            kafkaTemplateAvro.send(kafkaTopic, key, order);
            log.info("Sent: key: {}, value: {}", key, order);
        } catch (Exception e) {
            log.error("Message not generated caused by: {}", e.getMessage());
        }
    }
}
