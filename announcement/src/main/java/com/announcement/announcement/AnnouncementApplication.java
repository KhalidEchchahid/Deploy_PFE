package com.announcement.announcement;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {
                "com.announcement.announcement" ,
                "com.amqp.amqp" ,
        }
)
@EnableFeignClients(
        basePackages = "com.openfeign.openfeign"
)
public class AnnouncementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnouncementApplication.class, args);
    }

}
