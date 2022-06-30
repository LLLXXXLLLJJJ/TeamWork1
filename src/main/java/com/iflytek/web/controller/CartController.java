package com.iflytek.web.controller;

import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.pojo.User;
import com.iflytek.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")   // 认证 要登录  根据用户id来获取 购物车商品数据
@SessionAttributes(value = {"carts"})
public class CartController {

    @Autowired
    CartService cartService;

    @RequestMapping("")
    public String cartIndex(@AuthenticationPrincipal User user, Model model){
        // 显示购物车数据
        // 会话跟踪：
//        QueryWrapper<GoodsCart> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", user.getId());
        List<GoodsCart> carts = cartService.getByUserId(user.getId());
        model.addAttribute("carts", carts);
        return "cart";
    }

}
