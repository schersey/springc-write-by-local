package com.smart.web;

import com.smart.vo.Person;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanchangxian on 2018/12/17.
 */

@RestController
public class MyRibbonController {
    static final Logger log = LoggerFactory.getLogger(MyRibbonController.class);
    @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
    public Person findPersion(@PathVariable("personId") Integer personId, HttpServletRequest request){
        Person p = new Person();
        p.setId(personId);
        p.setName("yan-power1");
        p.setAge(30);
        p.setMessage(request.getRequestURI().toString());
        log.info("powe-1 request:"+ p.toString());
        return p;
    }


}
