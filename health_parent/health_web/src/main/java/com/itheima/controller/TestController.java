package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
public class TestController {

    @Reference
    TestService testService;

    @RequestMapping("/test")
    public String test(){
        return testService.test();

    }
}