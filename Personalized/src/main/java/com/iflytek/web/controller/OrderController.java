package com.iflytek.web.controller;

import com.iflytek.web.pojo.User;
import com.iflytek.web.service.CartService;
import com.iflytek.web.service.GoodsCartService;
import com.iflytek.web.service.OrderService;
import com.iflytek.web.viewmodel.OrderModel;
import com.iflytek.web.vo.CartMxVo;
import com.iflytek.web.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("order")   // 认证 要登录  根据用户id来获取 购物车商品数据
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 订单
     * 立即购买
     * */
    @ResponseBody
    @RequestMapping("/add/{goodsId}")
    public R addSingleGoods(@AuthenticationPrincipal User user, @PathVariable Integer goodsId
    ){
        //将数据保存到订单表中
        if(orderService.add(goodsId,user.getId())==1){
            return  R.ERROR();
        }
        return R.SUCCESS();
    }


    @ResponseBody
    @RequestMapping("/cart")
    public R addCartGoods(@AuthenticationPrincipal User user,@RequestBody List<CartMxVo>list){
        orderService.addAll(list,user.getId());
        return R.SUCCESS();
    }


    @RequestMapping("orderView")
    public String orderView(@AuthenticationPrincipal User user, Model model){
        Integer userId = user.getId();
        //根据用户id获取所有订单，包括订单明细
        List<OrderModel> carts=orderService.queryOrderByUserId(userId);

        model.addAttribute("carts", carts);
        return "order";
    }


}
