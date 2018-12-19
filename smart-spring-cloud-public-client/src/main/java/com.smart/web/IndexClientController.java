package com.smart.web;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.smart.service.IndexClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yanchangxian on 2018/12/14.
 */
@Controller
public class IndexClientController {
    static final Logger log = LoggerFactory.getLogger(IndexClientController.class);
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    IndexClientService indexClientService;

    /**
     * 利用restTemplate请求
     * @return
     */
    @RequestMapping("indexClient.do")
    @ResponseBody
    public String indexClient() {
        log.info("request");
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://microserver-1000-client/index.do", String.class);
        return indexClientService.helloIndexClinet();
    }
}
