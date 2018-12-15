package com.smart.web;

import com.sun.javafx.scene.control.skin.VirtualFlow;
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

    @ResponseBody
    @RequestMapping("/index.do")
    public List<Map<String, String>> index(){
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String > map = new HashMap<>();
        map.put("keyTest", "测试的值～");
        list.add(map);
        return list;
    }

}
