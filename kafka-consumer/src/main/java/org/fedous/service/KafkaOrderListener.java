package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.AvroOrder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOrderListener {

    /*
    @KafkaListener(topics = "testing-order")
    public void listen(NewOrder order) {

        log.info("Received message: {}", order);
    }

     */

    @KafkaListener(topics = "testing-order-avro", containerFactory = "kafkaListenerContainerFactoryAvro")
    public void listenAvro(@Header(name = "kafka_receivedPartitionId") int partition,
                           @Header(name = "kafka_offset") int offset,
                           @Payload AvroOrder order) {

        log.info("Partition {}, offset {}, message: {}", partition, offset, order);
    }
}
