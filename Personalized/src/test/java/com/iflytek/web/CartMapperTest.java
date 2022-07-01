package com.iflytek.web;

import com.iflytek.web.mapper.CartMapper;
import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.vo.CartVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartMapperTest {
    @Autowired
    private CartMapper cartMapper;
    @Test
    public void insert() {
        GoodsCart goodsCart = new GoodsCart();
        goodsCart.setUserId(99);
        goodsCart.setGoodsId(2);
        goodsCart.setNumber(3);
        Integer rows = cartMapper.insert(goodsCart);
        System.out.println("rows=" + rows);
    }

    @Test
    public void updateNumById() {
        Integer id = 1;
        Integer number = 20;
        Integer rows = cartMapper.updateNumById(id, number);
        System.out.println("rows=" + rows); }

    @Test
    public void findByUserIdAndGoodsId() {
        Integer userId = 1;
        Integer goodsId = 2;
        GoodsCart result = cartMapper.findByUserIdAndGoodsId(userId, goodsId);
        System.out.println(result);
    }




//    @Test
//    public void findVOByUserId() {
//        List<CartVO> list = cartMapper.findVOByUserId(8);
//        System.out.println(list); }

    @Test
    public void getByUserId() {
      List<GoodsCart> list = cartMapper.getByUserId(8);
      System.out.println(list); }




    @Test
    public void findById() {
        Integer id = 1;
        GoodsCart result = cartMapper.findById(id);
        System.out.println(result);
    }




    @Test
    public void findVoByIds() {
        Integer[] ids = {1, 2, 3, 13, 14, 16, 89};
        List<GoodsCart> list = cartMapper.findVOByIds(ids);
        System.out.println("count=" + list.size());
        for (GoodsCart item : list) {
            System.out.println(item);
        }
    }



    @Test
    public void deleteById() {
        Integer id = 85;
        cartMapper.deleteById(id);
    }




    @Test
    public void deleteByIds() {
        Integer[] ids = {11,12,13};
        cartMapper.deleteByIds(ids);
    }



}
