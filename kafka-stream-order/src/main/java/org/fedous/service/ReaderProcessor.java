package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.fedous.generated.AvroOrder;
import org.fedous.generated.CustomerOrder;
import org.fedous.generated.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReaderProcessor {

    @Value(value = "${spring.kafka.topics.avro-order}")
    private String avroOrderTopic;

    @Value(value = "${spring.kafka.topics.person}")
    private String personTopic;

    @Value(value = "${spring.kafka.topics.customer-order}")
    private String customerOrder;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        GlobalKTable<String, Person> people = streamsBuilder.globalTable(personTopic,
                Materialized.as("person-state-store"));

        KStream<String, AvroOrder> orderStream = streamsBuilder
                .stream(avroOrderTopic);

        KStream<String, CustomerOrder> outputStream = orderStream.join(people,
                        (k, v) -> v.getCustomerId(),
                        this::generateCustomerOrder);

        outputStream.to(customerOrder);
        outputStream.foreach((k, v) -> log.info("Key: {}, Value: {}", k, v.toString()));
    }

    CustomerOrder generateCustomerOrder(AvroOrder order, Person person) {
        return new CustomerOrder(order.getOrderId(), person.getId(), person.getName(), person.getSurname(), person.getAge(), order.getProductIds());
    }
}
