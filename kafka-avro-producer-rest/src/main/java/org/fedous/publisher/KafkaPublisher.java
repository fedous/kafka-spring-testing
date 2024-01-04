package org.fedous.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fedous.generated.OrderItem;
import org.fedous.generated.OrderStatus;
import org.fedous.generated.OrderType;
import org.fedous.generated.OrderValue;
import org.fedous.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisher {
    //private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, OrderValue> kafkaTemplate;

    public void producerKafkaMessage(String topicName, Order order) {
        OrderValue orderValue=new OrderValue();
        orderValue.setOrderID(order.getId());
        orderValue.setOrderDate(order.getOrderDate());
        orderValue.setOrderStatus(OrderStatus.valueOf(order.getOrderStatus().name()));
        orderValue.setTotalCost(order.getTotalCost());
        orderValue.setOrderType(OrderType.valueOf(order.getOrderType().name()));
        orderValue.setOrderItems(mapOrderItems(order.getOrderItem()));

        CompletableFuture<SendResult<String,OrderValue>> futureResult= kafkaTemplate.send(topicName, order.getId(), orderValue);

        futureResult.whenComplete((result,ex)->{
            if(ex == null){
                log.info("message send successfully to Kafka topic - {}, partition - {} offset- {}",
                        result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }else{
                log.error("Error sending message to the topic -{}, e- {}",topicName,ex);
            }
        });
    }

    private List<OrderItem> mapOrderItems(List<org.fedous.model.OrderItem> orderItem) {
        return orderItem.stream().map(v -> new OrderItem(v.getItemName(), v.getItemPrice())).collect(Collectors.toList());
    }
}
