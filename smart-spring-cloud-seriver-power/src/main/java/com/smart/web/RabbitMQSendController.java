package com.smart.web;

import com.smart.service.RabbitMQSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by yanchangxian on 2018/12/23.
 */
@RestController
public class RabbitMQSendController {
    @Autowired
    RabbitMQSendService rabbitMQSendService;

    @RequestMapping(value = "/sendRabbitMQ", method = RequestMethod.GET)
    public String sendRequest(){
        //创建消息
        Message msg = MessageBuilder.withPayload("Hello World".getBytes()).build();
        //发送消息
        rabbitMQSendService.sendOrder().send(msg);
        return "send ok";
    }
}
