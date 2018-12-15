package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by yanchangxian on 2018/12/15.
 */

@EnableEurekaServer
@SpringBootApplication
public class ApplicationEureka {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationEureka.class, args);
    }
}
