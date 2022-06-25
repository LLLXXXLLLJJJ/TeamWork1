package com.iflytek.web.controller;

import com.iflytek.web.pojo.User;
import com.iflytek.web.service.GoodsService;
import com.iflytek.web.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/recommendation")
    public R recommendation(@AuthenticationPrincipal User user){
        R r = R.SUCCESS();
        r.setData(goodsService.queryRecommendation(user.getId()));
        return r;
    }
}
