package org.fedous;

import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.AvroOrder;
import org.fedous.generated.Person;
import org.fedous.service.KafkaOrderSender;
import org.fedous.service.KafkaPersonSender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

@SpringBootApplication
@Slf4j
public class KafkaProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }


    @Bean
    @Order(1)
    CommandLineRunner senderPersons(KafkaPersonSender sender) {
        return args -> {

            Person[] people = {
                    new Person("1", "John", "Doe", 30),
                    new Person("2", "Pippo", "Doe", 5),
                    new Person("3", "Pluto", "Cerri", 15),
                    new Person("4", "Asdrubale", "Rossi", 43),
                    new Person("5", "Gianfranco", "Pizziroli", 62),
                    new Person("6", "Bambo", "Scemo", 216),
                    new Person("7", "Gion", "Tavola", 95),
                    new Person("8", "Cacca", "Pupù", 3),
                    new Person("9", "Gino", "Vino", 23),
                    new Person("10", "Fido", "Diesel", 11),
            };

            Arrays.stream(people).forEach(sender::send);

            log.info("Persons topic correctly loaded");

        };
    }

    @Bean
    @Order(2)
    CommandLineRunner senderAvro(KafkaOrderSender sender) {
        return args -> {

            Random random = new Random();
            while(true) {
                try {
                    int key = random.ints(1,1, Integer.MAX_VALUE).findFirst().orElseThrow(); // key value
                    int orderId = random.ints(1, 1, 11).findFirst().orElseThrow(); // orderId (1 to 10)
                    int numProducts = random.ints(1, 1, 21).findFirst().orElseThrow(); // num of products (1 to 20)
                    List<Long> productIds = random.longs(numProducts,0L,100L).distinct().boxed().toList(); // ids of products (0 to 99)

                    AvroOrder order = new AvroOrder();
                    order.setOrderId(key);
                    order.setCustomerId(String.valueOf(orderId));
                    order.setProductIds(productIds);

                    sender.sendAvro(String.valueOf(key), order);

                    sleep(Duration.ofSeconds(5).toMillis());
                } catch (Exception e) {
                    log.error("Error during message creation. Exception: {}", e.getMessage());
                }
            }
        };
    }
}
