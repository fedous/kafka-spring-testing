package org.fedous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaAvroProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaAvroProducerApplication.class, args);
    }
}