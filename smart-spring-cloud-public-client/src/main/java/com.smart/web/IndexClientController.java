package com.smart.web;

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

    @Autowired
    RestTemplate restTemplate;

    /**
     * 利用restTemplate请求
     * @return
     */
    @RequestMapping("indexClient.do")
    @ResponseBody
    public String indexClient() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8888/index.do", String.class);
        return responseEntity.getBody();
    }
}
