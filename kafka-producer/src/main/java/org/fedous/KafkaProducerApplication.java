package org.fedous;

import org.fedous.commons.NewOrder;
import org.fedous.service.KafkaOrderSender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class KafkaProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }

    @Bean
    CommandLineRunner sender(KafkaOrderSender sender) {
        return args -> {

            int i = 0;
            while(true) {
                i++;

                NewOrder order = new NewOrder();
                order.setCustomerName("Pippo Nr " + i);
                List<Long> productIds = new ArrayList<>();
                productIds.add(System.currentTimeMillis());
                productIds.add(7L*i);
                productIds.add(9L*i);
                order.setProductIds(productIds);

                sender.send(order);

                sleep(Duration.ofSeconds(5).toMillis());
            }
            /*
            int i = 0;
            while (true) {
                i++;
                sender.send("Message nbr " + i);
                sleep(5000);

            }
            */

        };
    }
}
