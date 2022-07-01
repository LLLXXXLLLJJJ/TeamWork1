package com.iflytek.web;

import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.service.CartService;
import com.iflytek.web.service.ex.ServiceException;
import com.iflytek.web.vo.CartVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;

    @Test
    public void addToCart() {
        try {
            Integer userId = 200;
            Integer goodsId = 3465001;
            Integer amount = 1;
            cartService.addToCart(userId, goodsId, amount);
            System.out.println("OK.");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

//    @Test
//    public void addToCart() {
//        cartService.addToCart(8, 1078001, 5);
//    }


//@Test
//public void getVOByUserId() {
//        List<CartVO> list = cartService.getVOByUserId(8);
//        System.out.println("count=" + list.size());
//        for (CartVO item : list) {
//            System.out.println(item);
//        }
//    }

    @Test
    public void getByUserId() {
        List<GoodsCart> list = cartService.getByUserId(8);
        System.out.println("count=" + list.size());
        for (GoodsCart item : list) {
            System.out.println(item);
        }
    }


    @Test
    public void addNum() {
        try {
            Integer id = 89;
            Integer userId = 8;
            Integer number = cartService.addNum(id, userId);
            System.out.println("OK. New num=" + number);
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void findVoByIds() {
        Integer[] ids = {1, 2, 3, 13, 14, 16, 89};
        Integer userId = 8;
        List<GoodsCart> list = cartService.getVOByIds(userId,ids);
        System.out.println("count=" + list.size());
        for (GoodsCart item : list) {
            System.out.println(item);
        }
    }


    @Test
    public void delete() {
        cartService.delete(87,200);
    }


    @Test
    public void deleteAll() {
        Integer[] ids = {21,25};
        cartService.deleteAll(ids);
    }


}
