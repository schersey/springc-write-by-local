package com.smart.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yanchangxian on 2018/12/18.
 */

@FeignClient(value = "MICROSERVER-1000-CLIENT", fallback = FeignService.FallBack.class)
public interface FeignService {
    @RequestMapping("/index.do")
    String testFeign();

    //如果失败 则走fallback方法
    @Component
    static class FallBack implements FeignService{

        @Override
        public String testFeign() {
            return "error hello";
        }
    }
}
