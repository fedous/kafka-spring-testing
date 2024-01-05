package org.fedous.service;

import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaPersonSender {

    @Value(value = "${spring.kafka.topics.person}")
    private String personTopic;
    @Autowired
    KafkaTemplate<String, Person> kafkaTemplatePerson;

    public void send(Person person) {
        try {
            kafkaTemplatePerson.send(personTopic, person.getId(), person);
            log.info("Sent - key: {}, value: {}", person.getId(), person);
        } catch (Exception e) {
            log.error("Error sending person: {} with exception: {}", person, e.getMessage());
        }
    }
}
