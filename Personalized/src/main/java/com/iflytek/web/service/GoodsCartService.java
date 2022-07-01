package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.CartMapper;
import com.iflytek.web.mapper.GoodsCartMapper;
import com.iflytek.web.pojo.Goods;
import com.iflytek.web.pojo.GoodsCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class GoodsCartService extends ServiceImpl<GoodsCartMapper, GoodsCart> {
    @Resource
    private GoodsCartMapper goodsCartMapper;

    public List<GoodsCart> getByUserId(Integer userId){
        return goodsCartMapper.getByUserId(userId);
    }
}
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