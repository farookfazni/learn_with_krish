package com.faznifarook.order;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    @Autowired
    KafkaTemplate<String,Order> kafkaTemplate;
    public void placeOrder(OrderRequest orderRequest) {

        Order order = Order.builder()
                .status(orderRequest.status())
                .allocAmount(orderRequest.allocAmount())
                .build();
//        todo : check the allocation is available or not

        Message<Order> message = MessageBuilder.withPayload(order)
                .setHeader(KafkaHeaders.TOPIC,"mainTopic")
                        .build();
        //  Storing to database
        orderRepository.save(order);
        //  Sending to kafka Topic
//        kafkaTemplate.send("mainTopic", order);
        kafkaTemplate.send(message);
    }

//    public Order findOrder(Integer id){
//        Optional<Order> orderResponse = orderRepository.findById(id);
//        Order order = orderResponse.get();
//        return order;
//    }
}
