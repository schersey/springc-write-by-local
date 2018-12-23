package com.smart.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by yanchangxian on 2018/12/23.
 */
public interface RabbitMQSendService {

    @Output("myInput")
    SubscribableChannel sendOrder();

}
