package com.iflytek.web.controller;

import com.iflytek.web.pojo.User;
import com.iflytek.web.service.GoodsService;
import com.iflytek.web.service.UserService;
import com.iflytek.web.vo.R;
import org.apache.catalina.security.SecurityConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping("/recommendation")
    public R recommendation(@AuthenticationPrincipal User user){
        R r = R.SUCCESS();
        r.setData(goodsService.queryRecommendation(user.getId()));
        return r;

    }

    @RequestMapping(value = "/check/{regName}",method = {RequestMethod.POST})
    @ResponseBody
    public R checkName(@PathVariable(value = "regName") String regName){
        R r=R.ERROR();
        System.out.println("checkName:"+regName);

        if(!userService.userNameExists(regName)) { //注册名不存在,通过验证，返回true
            r = R.SUCCESS();
        }

        return r;
    }
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    @ResponseBody
    public R register(@RequestBody User user){//实现新增用户的功
        R r = R.ERROR();
        if(userService.addUser(user.getUserName(), passwordEncoder.encode(user.getPassword()))){
            r=R.SUCCESS();
        }
        return r;
    }
}
