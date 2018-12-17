package com.smart.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yanchangxian on 2018/12/17.
 */

@RestController
public class MyRibbonNewTestController {
    static final Logger log = LoggerFactory.getLogger(MyRibbonNewTestController.class);
    @Autowired
    private RestTemplate restTemplate;

    @ResponseBody
    @RequestMapping("/testNewRibbonClient.do")
    public String indexClient() {
        log.info("request");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://microserver-1000-client/person/1", String.class);
        return responseEntity.getBody();
    }

//    public static void main(String[] args) throws Exception {
//        //设置请求服务器
//        ConfigurationManager.getConfigInstance().setProperty("my-client.ribbon.listOfServers","MICROSERVER-1000-CLIENT");
//        //Rest请求客户端
//        RestClient client = (RestClient) ClientFactory.getNamedClient("my-client");
//        //创建请求实例
//        HttpRequest request = HttpRequest.newBuilder().uri("/persion/1").build();
//        //发送多次请求
//        for(int i=0;i<10;i++){
//            HttpResponse response = client.executeWithLoadBalancer(request);
//            String result = response.getEntity(String.class);
//            System.out.println(result);
//        }
//    }

}
