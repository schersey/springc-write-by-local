package com.smart.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by yanchangxian on 2018/12/23.
 */
public interface RabbitMQGetService {

    @Input("myInput")
    SubscribableChannel myInput();

}
