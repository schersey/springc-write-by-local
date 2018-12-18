package com.smart.web;

import com.smart.service.FeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanchangxian on 2018/12/18.
 */
@RestController
public class FeignController {

    static final Logger log = LoggerFactory.getLogger(FeignController.class);

    @Autowired
    FeignService feignService;

    @RequestMapping(value = "/testFeign.do", method = RequestMethod.GET)
    private String feign(){
        return feignService.testFeign();
    }
}
