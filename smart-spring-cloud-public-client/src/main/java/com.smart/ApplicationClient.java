package com.smart;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.smart.service.RabbitMQGetService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;

/**
 * Created by yanchangxian on 2018/12/14.
 */


//@EnableHystrix
@EnableCircuitBreaker //hystrix
@EnableHystrixDashboard //hystrix控制台
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient

@EnableZuulProxy  //zuul
@EnableBinding(RabbitMQGetService.class)
public class ApplicationClient {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient.class, args);
    }


    @StreamListener("myInput")
    public void receive(byte[] msg) {
        System.out.println("get msg:" + new String(msg));
    }
}
