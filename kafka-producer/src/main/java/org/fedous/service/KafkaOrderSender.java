package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.commons.NewOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOrderSender {

    @Autowired
    KafkaTemplate<String, NewOrder> kafkaTemplate;

    public void send(NewOrder order) {

        String kafkaTopic = "testing-order";
        kafkaTemplate.send(kafkaTopic, order);
        log.info("Sent: {}", order);
    }
}
