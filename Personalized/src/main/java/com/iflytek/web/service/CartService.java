package com.iflytek.web.service;

import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.vo.CartVO;

import java.util.List;

//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.iflytek.web.mapper.CartMapper;
//import com.iflytek.web.pojo.GoodsCart;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Service
//public class CartService extends ServiceImpl<CartMapper, GoodsCart> {
//
//    @Resource
//    private CartMapper cartMapper;
//
//    public List<GoodsCart> getByUserId(Integer userId){
//        return cartMapper.getByUserId(userId);
//    }
//}
public interface CartService  {

void addToCart(Integer userId, Integer goodsId, Integer amount);


//List<CartVO> getVOByUserId(Integer userId);
List<GoodsCart> getByUserId(Integer userId);


Integer addNum(Integer id, Integer userId);


//List<CartVO> getVOByIds(Integer userId, Integer[] ids);
List<GoodsCart> getVOByIds(Integer userId, Integer[] ids);


void delete(Integer id, Integer userId);

int deleteAll(Integer[]ids);


}

