package com.smart.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yanchangxian on 2018/12/14.
 */

@Configuration
public class ClientConfig {
//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory(){
//        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
//        factory.setPort(9999);
//        return factory;
//    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
