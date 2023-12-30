package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.commons.NewOrder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOrderListener {

    @KafkaListener(topics = "testing-order")
    public void listen(NewOrder order) {

        log.info("Received message: {}", order);
    }
}
