package com.smart.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yanchangxian on 2018/12/18.
 */

@FeignClient(value = "MICROSERVER-1000-CLIENT")
public interface FeignService {
    @RequestMapping("/index.do")
    String testFeign();
}
