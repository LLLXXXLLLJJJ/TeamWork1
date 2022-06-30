package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.CartMapper;
import com.iflytek.web.pojo.GoodsCart;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartService extends ServiceImpl<CartMapper, GoodsCart> {

    @Resource
    private CartMapper cartMapper;

    public List<GoodsCart> getByUserId(Integer userId){
        return cartMapper.getByUserId(userId);
    }
}
