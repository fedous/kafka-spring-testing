package org.fedous.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@EnableKafkaStreams
@Configuration
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;
    @Value(value = "${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;
    @Value(value = "${spring.kafka.topics.avro-order}")
    private String avroOrderTopic;
    @Value(value = "${spring.kafka.topics.person}")
    private String personTopic;
    @Value(value = "${spring.kafka.topics.order-enriched}")
    private String orderEnrichedTopic;
    @Value(value = "${spring.kafka.topics.customer-summary}")
    private String customerSummaryTopic;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 3);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
        // configure the state location to allow tests to use clean state for every run
        //props.put(StreamsConfig.STATE_DIR_CONFIG, stateStoreLocation);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    NewTopic inputTopic() {
        return TopicBuilder.name("input-topic")
                .partitions(3)
                .replicas(3)
                .build();
    }


    @Bean
    public NewTopic topicAvroOrder() {
        return new NewTopic(avroOrderTopic, 3, (short) 3);
    }
    @Bean
    public NewTopic topicPerson() {
        return TopicBuilder.name(personTopic)
                .partitions(3)
                .replicas(3)
                .compact()
                .build();
    }
    @Bean
    public NewTopic topicOrderEnriched() {
        return new NewTopic(orderEnrichedTopic, 3, (short) 3);
    }

    @Bean
    public NewTopic topicCustomerSummary() {
        return new NewTopic(customerSummaryTopic, 3, (short) 3);
    }
}
