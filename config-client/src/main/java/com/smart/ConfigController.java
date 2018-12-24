package com.smart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanchangxian on 2018/12/24.
 */
@RefreshScope
@RestController
public class ConfigController {

    @Value("${from}")
    private String from;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @RequestMapping("/from")
    public String from() {
        return this.from + "~user:" + this.username + "~pass:" + this.password;
    }
}