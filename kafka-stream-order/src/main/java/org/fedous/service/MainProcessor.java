package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.fedous.generated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
public class MainProcessor {

    @Value(value = "${spring.kafka.topics.avro-order}")
    private String avroOrderTopic;
    @Value(value = "${spring.kafka.topics.person}")
    private String personTopic;
    @Value(value = "${spring.kafka.topics.order-enriched}")
    private String orderEnrichedTopic;
    @Value(value = "${spring.kafka.topics.customer-summary}")
    private String customerSummaryTopic;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        GlobalKTable<String, Person> people = streamsBuilder.globalTable(personTopic,
                Materialized.as("person-state-store"));

        KStream<String, AvroOrder> orderStream = streamsBuilder
                .stream(avroOrderTopic);

        KStream<String, OrderEnriched> orderEnrichedStream = orderStream.join(people,
                        (k, v) -> v.getCustomerId(),
                        this::generateCustomerOrder);
        orderEnrichedStream.to(orderEnrichedTopic);
        //orderEnrichedStream.peek((k, v) -> log.info("Key: {}, Value: {}", k, v.toString()));

        KTable<String, CustomerSummary> customerSummaryKTable =
                orderEnrichedStream.groupBy((k,v) -> v.getCustomerId())
                        .aggregate(
                                () -> new CustomerSummary("", "", "", 0, 0L, new ProductStatistics(0L, 0F)),
                                (key, value, aggregate) -> aggregateCustomerSummary(value,  aggregate));

        customerSummaryKTable.toStream().peek((k, v) -> log.info("Key: {}, Value: {}", k, v.toString()));
        customerSummaryKTable.toStream().to(customerSummaryTopic);
    }

    OrderEnriched generateCustomerOrder(AvroOrder order, Person person) {
        return new OrderEnriched(order.getOrderId(), person.getId(), person.getName(), person.getSurname(), person.getAge(), order.getProductIds());
    }

    CustomerSummary aggregateCustomerSummary(OrderEnriched orderEnriched, CustomerSummary aggregate) {
        return new CustomerSummary(
                orderEnriched.getCustomerId(),
                orderEnriched.getCustomerName(),
                orderEnriched.getCustomerSurname(),
                orderEnriched.getCustomerAge(),
                aggregate.getNumOrders() + 1,
                aggregateProductStatistics(orderEnriched.getProductIds(), aggregate.getProductsStatistics(), aggregate.getNumOrders() + 1));
    }

    ProductStatistics aggregateProductStatistics(List<Long> productIds, ProductStatistics aggregate, Long numOrders) {
        long numCurrProductIds = productIds.size();
        BigDecimal numProducts = new BigDecimal(aggregate.getNumProducts() + numCurrProductIds);
        BigDecimal numOrdersFloat = new BigDecimal(numOrders);
        return new ProductStatistics(
                aggregate.getNumProducts() + numCurrProductIds,
                (numProducts.divide(numOrdersFloat, 2, RoundingMode.HALF_DOWN).floatValue()));
    }
}
