package com.iflytek.web.controller;

import com.iflytek.web.pojo.User;
import com.iflytek.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {
    @Autowired
    UserService userService;

    @RequestMapping("/hello")
    public List<User> sayHello(){
        return userService.list();
    }
}
