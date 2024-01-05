package org.fedous.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.topics.avro-order}")
    private String avroOrderTopic;
    @Value(value = "${spring.kafka.topics.person}")
    private String personTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
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
}
