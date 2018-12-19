package com.smart.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yanchangxian on 2018/12/19.
 */

@Service
public class IndexClientService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloError")
    public String helloIndexClinet(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://microserver-1000-client/index.do", String.class);
        return responseEntity.getBody();
    }

    public String helloError(){
        return "PageDown";
    }
}
