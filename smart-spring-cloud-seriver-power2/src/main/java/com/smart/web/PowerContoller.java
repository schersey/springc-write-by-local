package com.smart.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanchangxian on 2018/12/14.
 */
@Controller
public class PowerContoller {
    static final Logger log = LoggerFactory.getLogger(PowerContoller.class);
    @ResponseBody
    @RequestMapping("/index.do")
    public List<Map<String, String>> index(){
        log.info("1001");
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String > map = new HashMap<>();
        map.put("keyTest2", "测试2的值～");
        map.put("port", "1001");
        list.add(map);
        return list;
    }

}
