package com.iflytek.web.controller;

import com.iflytek.web.pojo.Goods;
import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.pojo.User;
import com.iflytek.web.service.CartService;
import com.iflytek.web.service.GoodsCartService;
import com.iflytek.web.service.Impl.CartServiceImpl;
import com.iflytek.web.util.JsonResult;
import com.iflytek.web.vo.CartVO;
import com.iflytek.web.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Ids;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("cart")   // 认证 要登录  根据用户id来获取 购物车商品数据
@SessionAttributes(value = {"carts"})
public class CartController extends BaseController {
    @Autowired
    private CartService cartService;

    @Autowired
    private GoodsCartService goodsCartService;

    @ResponseBody
    @RequestMapping("addCart")
    public JsonResult<Void> addToCart(Integer goodsId,Integer amount, HttpSession session) {
        System.out.println("goodsId=" + goodsId);
        System.out.println("amount=" + amount);
        // 从Session中获取uid和username
        //Integer userId = getUserIdFromSession(session);
        //String username = getUsernameFromSession(session);
        Integer userId = getUserIdFromSession(session);
        // 调用业务对象执行添加到购物车
        cartService.addToCart(userId, goodsId, amount);
        // 返回成功
        return new JsonResult<Void>(OK); }



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

    /**
     * 删除
     * */
    @ResponseBody
    @RequestMapping("deleteCartByIds/{id}")
    public JsonResult<Void> delete(@PathVariable("id") Integer id, HttpSession session) {
        Integer userId = getUserIdFromSession(session);
        cartService.delete(id, userId);
        return new JsonResult<Void>(OK);
    }

    /**
     * 批量删除
     * */
    @ResponseBody
    @RequestMapping("deleteIds/{ids}")
    public JsonResult<Void> deleteAll(@PathVariable("ids")Integer[] ids) {
        cartService.deleteAll(ids);
        return new JsonResult<Void>(OK);
    }

}
