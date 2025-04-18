package com.notification.notification;

import com.amqp.amqp.RabbitMQMessageProducer;
import com.notification.notification.config.NotificationConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
        scanBasePackages = {
                "com.notification.notification",
                "com.amqp.amqp",
        }
)
@EnableFeignClients(
        basePackages = "com.openfeign.openfeign"
)
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(
//            RabbitMQMessageProducer producer ,
//            NotificationConfig notificationConfig
//    ){
//       return args -> {
//            producer.publish(new Person("khalid" , 20) ,
//                    notificationConfig.getInternalExchange() ,
//                    notificationConfig.getInternalNotificationRoutingKey()
//            );
//       } ;
//    }

    record Person(String name , int age){}
}
