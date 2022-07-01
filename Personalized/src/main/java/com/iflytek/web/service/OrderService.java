package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.OrderMapper;
import com.iflytek.web.pojo.Goods;
import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.pojo.Order;
import com.iflytek.web.pojo.OrderMx;
import com.iflytek.web.util.UuidUtils;
import com.iflytek.web.viewmodel.OrderModel;
import com.iflytek.web.vo.CartMxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderMxService orderMxService;
    @Autowired
    private GoodsCartService goodsCartService;

    public int add (Integer goodsId, Integer userId){
        Goods goods=goodsService.getById(goodsId);
        Order order=new Order();
        order.setMoney((double) goods.getGoodsPrice());
        order.setCreateTime(new Date());
        order.setUserId(userId);
        byte status = 1;
        order.setStatus(status);
        //String orderNo = UuidUtils.getUuid32();
        //System.out.println(orderNo);
        order.setOrderNo(UuidUtils.getUuid32()) ;
        this.save(order);

        OrderMx mx=new OrderMx();
        mx.setCreator(userId);
        mx.setOrderId(order.getId());
        mx.setGoodsId(goodsId);
        mx.setNum(1);
        mx.setCreateTime(new Date());
        mx.setOrderNo(order.getOrderNo());
        orderMxService.save(mx);
        return 1 ;
    }


    public boolean addAll(List<CartMxVo>list,Integer userId){
        //{cartId:70, num= 1 ,price:** , goodId:**}
        Order order=new Order();
        order.setCreateTime(new Date());
        order.setUserId(userId);
        byte status = 1;
        order.setStatus(status);
        order.setOrderNo(UuidUtils.getUuid32());
        order.setMoney(0D);

        List<OrderMx>mxList=new ArrayList<>();
        List<GoodsCart>cartList = new ArrayList<>();
        list.stream().forEach(p->{
            OrderMx mx=new OrderMx();
            mx.setCreator(userId);
//            mx.setOrderId(order.getId());
            mx.setGoodsId(p.getGoodsId());
            mx.setNum(p.getNum());
            mx.setCreateTime(new Date());
            mx.setOrderNo(order.getOrderNo());
            mxList.add(mx);

            order.setMoney(order.getMoney()+ p.getPrice()*p.getNum());

            GoodsCart goodsCart = new GoodsCart();
            goodsCart.setId(p.getCartId());
            goodsCart.setStatus(0);
            cartList.add(goodsCart);
        });
        this.save(order);

        mxList.stream().forEach(p->{
            p.setOrderId(order.getId());
        });
        orderMxService.saveBatch(mxList);

        //更新购物车状态
        goodsCartService.updateBatchById(cartList);

        return true;
    }

    public List<OrderModel>queryOrderByUserId(Integer userId){
        //构造list
        return this.baseMapper.queryOrderByUserId(userId);
    }
}
