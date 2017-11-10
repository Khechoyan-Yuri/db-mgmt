package com.sweng.customerqueue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by vikramh on 4/7/17.
 */
@Controller
public class CustomerQueueController {
    @RequestMapping("/")
    public String printQueue() {
        return "home";
    }
}
