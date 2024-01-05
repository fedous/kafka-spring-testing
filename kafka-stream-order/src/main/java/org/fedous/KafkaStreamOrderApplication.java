package org.fedous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaStreamOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamOrderApplication.class, args);
    }
}