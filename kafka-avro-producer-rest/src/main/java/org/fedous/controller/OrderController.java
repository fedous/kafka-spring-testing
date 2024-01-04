package org.fedous.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fedous.model.Order;
import org.fedous.publisher.KafkaPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final KafkaPublisher kafkaPublisher;

    @PostMapping("/order/create")
    public ResponseEntity<String> createOrder(@RequestBody Order order){
        try {
            kafkaPublisher.producerKafkaMessage("order", order);
            return new ResponseEntity<>("Message successfully sent to kafka topic", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error("Error sending message to kafka topic, exception = {}", e.getMessage());
            return new ResponseEntity<>("Error sending message to kafka topic", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
