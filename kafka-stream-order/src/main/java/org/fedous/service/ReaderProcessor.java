package org.fedous.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.fedous.generated.AvroOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReaderProcessor {

    @Value(value = "${spring.kafka.topics.avro-order}")
    private String avroOrderTopic;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, AvroOrder> orderStream = streamsBuilder
                .stream(avroOrderTopic);

        orderStream.foreach((k, v) -> log.info("Key: {}, Value: {}", k, v.toString()));
    }
}
