package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.OrderMapper;
import com.iflytek.web.pojo.Goods;
import com.iflytek.web.pojo.Order;
import com.iflytek.web.pojo.OrderMx;
import com.iflytek.web.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderMxService orderMxService;

    @Transactional()
    public int add (Integer goodsId){
        Goods goods = goodsService.getById(goodsId);
        Order order = new Order();
        order.setMoney(goods.getGoodsPrice());
        order.setCreateTime(new Date());
        //order.setUserId(UserId);
        byte status = 1;
        order.setStatus(status);
        order.setOrderNo(UuidUtils.getUuid36());

        this.save(order);


        OrderMx mx = new OrderMx();

        //mx.setCreator(userId);
        mx.setOrderId(order.getId());
        mx.setGoodsId(goodsId);
        mx.setNum(1);
        mx.setCreateTime(new Date());
        mx.setOrderNo(order.getOrderNo());
        orderMxService.save(mx);
        return 1;










    }
}
