package com.iflytek.web.controller;

import com.iflytek.web.pojo.Order;
import com.iflytek.web.pojo.User;
import com.iflytek.web.vo.R;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @RequestMapping("/add/{goodsId}")
    public R addSingleGoods(@AuthenticationPrincipal User user,
                            @PathVariable Integer goodsId){
            R r = R.SUCCESS();

            return r;


    }


}
