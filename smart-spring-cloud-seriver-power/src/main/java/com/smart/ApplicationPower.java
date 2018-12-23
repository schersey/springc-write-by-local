package com.smart;

import com.smart.service.RabbitMQSendService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * Created by yanchangxian on 2018/12/14.
 */

@SpringBootApplication
@EnableEurekaClient
@EnableBinding(RabbitMQSendService.class)
public class ApplicationPower {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationPower.class);
    }
}
