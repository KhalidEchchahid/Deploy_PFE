package com.notification.notification.rabbitmq;

import com.notification.notification.dto.NotificationDTO;
import com.notification.notification.services.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;
    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificationDTO notificationDTO){
        log.info("Consumer {} from queue" , notificationDTO);
        notificationService.creatNotification(notificationDTO);
    }
}
