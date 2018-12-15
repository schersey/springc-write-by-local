package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by yanchangxian on 2018/12/14.
 */
@SpringBootApplication
@EnableEurekaClient
public class ApplicationClient {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient.class);
    }
}
