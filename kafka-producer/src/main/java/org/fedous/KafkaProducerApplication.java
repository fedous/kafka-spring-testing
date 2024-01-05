package org.fedous;

import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.AvroOrder;
import org.fedous.service.KafkaOrderSender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

@SpringBootApplication
@Slf4j
public class KafkaProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }

    /*
    @Bean
    @Order(1)
    CommandLineRunner senderOneMessage(KafkaOrderSender sender) {
        return args -> {

            int i = 99999;
            AvroOrder order = new AvroOrder();
            order.setCustomerName("Pippo Nr " + i);
            List<Long> productIds = new ArrayList<>();
            productIds.add(System.currentTimeMillis());
            productIds.add(7L*i);
            productIds.add(9L*i);
            order.setProductIds(productIds);

            sender.sendAvro(order);

            sleep(Duration.ofSeconds(10).toMillis());
            log.info("Sender one message ended");

        };
    }

     */

    @Bean
    //@Order(2)
    CommandLineRunner senderAvro(KafkaOrderSender sender) {
        return args -> {

            Random random = new Random();
            //int i = 0;
            while(true) {
                try {
                    IntStream intStream = random.ints();
                    int key = intStream.filter(i -> i >= 0).limit(1).findAny().orElseThrow();
                    AvroOrder order = new AvroOrder();
                    order.setCustomerName("Pippo Nr " + key);
                    List<Long> productIds = new ArrayList<>();
                    productIds.add(System.currentTimeMillis());
                    productIds.add(7L * key % 10);
                    productIds.add(9L * key % 100);
                    order.setProductIds(productIds);

                    sender.sendAvro(key, order);

                    sleep(Duration.ofSeconds(5).toMillis());
                } catch (Exception e) {
                    log.error("Error during message creation. Exception: {}", e.getMessage());
                }
            }
        };
    }
}
