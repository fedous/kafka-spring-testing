package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.AvroOrder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOrderListener {

    @KafkaListener(topics = "${spring.kafka.topics.avro-order}", containerFactory = "kafkaListenerContainerFactoryAvro")
    public void listenAvro(@Header(name = KafkaHeaders.RECEIVED_PARTITION) int partition,
                           @Header(name = KafkaHeaders.OFFSET) int offset,
                           @Payload AvroOrder order) {

        log.info("Partition {}, offset {}, message: {}", partition, offset, order);
    }

    /*
    PER LEGGERE PERSON AL BISOGNO
    @KafkaListener(topics = "testing-person", containerFactory = "kafkaListenerContainerFactoryPerson")
    public void listenAvro(@Header(name = KafkaHeaders.RECEIVED_PARTITION) int partition,
                           @Header(name = KafkaHeaders.OFFSET) int offset,
                           @Header(name = KafkaHeaders.RECEIVED_KEY) String key,
                           @Payload Person person) {

        log.info("Partition {}, offset {}, key {}, message: {}", partition, offset, key, person);
    }

     */
}
