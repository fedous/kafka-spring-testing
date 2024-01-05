package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.AvroOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOrderSender {

    @Value(value = "${spring.kafka.topics.avro-order}")
    private String avroOrderTopic;
    @Autowired
    KafkaTemplate<String, AvroOrder> kafkaTemplateAvro;

    public void sendAvro(String key, AvroOrder order) {

        try {
            kafkaTemplateAvro.send(avroOrderTopic, key, order);
            log.info("Sent: key: {}, value: {}", key, order);
        } catch (Exception e) {
            log.error("Message not generated caused by: {}", e.getMessage());
            log.error("Message: key {}, value {}", key, order);
        }
    }


}
