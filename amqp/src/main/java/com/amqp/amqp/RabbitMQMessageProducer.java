package com.amqp.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RabbitMQMessageProducer {
    private final AmqpTemplate amqpTemplate;


    public void publish(Object payload , String exchange ,String routingKey ){
        log.info("Publishing to {} using routingKey {} . payload : {}" ,exchange,routingKey,payload );
        amqpTemplate.convertAndSend(exchange , routingKey , payload);
        log.info("Published to {} using routingKey {} . payload : {}" ,exchange,routingKey,payload );

    }
}
