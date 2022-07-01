package com.iflytek.web.service.Impl;

import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.pojo.Goods;
import com.iflytek.web.mapper.CartMapper;
import com.iflytek.web.service.CartService;
import com.iflytek.web.service.GoodsService;
import com.iflytek.web.service.ex.*;
import com.iflytek.web.viewmodel.Goods4List;
import com.iflytek.web.vo.CartVO;
import com.iflytek.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/** 处理购物车数据的业务层实现类 */
@Service
public class CartServiceImpl implements CartService  {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private GoodsService goodsService;

    @Override
    public void addToCart(Integer userId, Integer goodsId, Integer amount) {
        GoodsCart result = cartMapper.findByUserIdAndGoodsId(userId, goodsId);
        Date now = new Date();
        // 判断查询结果是否为null
        if (result == null) {
            GoodsCart goodsCart = new GoodsCart();
            goodsCart.setUserId(userId);
            goodsCart.setGoodsId(goodsId);
            goodsCart.setNumber(amount);
            //查询商品数据，得到商品价格
            Goods item = goodsService.getById(goodsId);
            goodsCart.setGoodsPrice(item.getGoodsPrice());
            goodsCart.setCreateTime(now);
            // 调用insert(cart)执行将数据插入到数据表中
            Integer rows = cartMapper.insert(goodsCart);
            if (rows != 1) {
                throw new InsertException("插入商品数据时出现未知错误，请联系系统管理员");
            }
        } else {
            Integer id = result.getId();
            // 得到新的数量
            Integer number = result.getNumber() + amount;
            // 执行更新数量
            Integer rows = cartMapper.updateNumById(id, number);
            if (rows != 1) {
                throw new InsertException("修改商品数量时出现未知错误，请联系系统管理员");
            }
        }
    }


        public List<GoodsCart> getByUserId(Integer userId){
        return cartMapper.getByUserId(userId);
    }



    @Override
    public Integer addNum(Integer id, Integer userId) {
        GoodsCart result = cartMapper.findById(id);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出CartNotFoundException
            throw new CartNotFoundException("尝试访问的购物车数据不存在");
        }

        // 判断查询结果中的uid与参数uid是否不一致
        if (!result.getUserId().equals(userId)) {
            // 是：抛出AccessDeniedException
            throw new AccessDeniedException("非法访问");
        }
        Integer number = result.getNumber() + 1;

        Integer rows = cartMapper.updateNumById(id, number);
        if (rows != 1) {
            throw new InsertException("修改商品数量时出现未知错误，请联系系统管理员");
        }
        // 返回新的数量
        return number;
    }


    @Override
    public List<GoodsCart> getVOByIds(Integer userId, Integer[] ids) {
        List<GoodsCart> list = cartMapper.findVOByIds(ids);
		/**
        for (CartVO cart : list) {
			if (!cart.getUid().equals(uid)) {
				list.remove(cart);
			}
		}
		*/
        Iterator<GoodsCart> it = list.iterator();
        while (it.hasNext()) {
            GoodsCart cart = it.next();
            if (!cart.getUserId().equals(userId)) {
                it.remove();
            }
        }
        return list;
    }



    @Transactional
    @Override
    public void delete(Integer id, Integer userId) {
        GoodsCart result = cartMapper.findById(id);
        // 判断查询结果是否为null
        if (result == null) {
            // 是：抛出CartNotFoundException
            throw new CartNotFoundException("尝试访问的购物车数据不存在");
        }
        if (!result.getUserId().equals(userId)) {
            // 是：抛出AccessDeniedException
            throw new AccessDeniedException("非法访问");
        }
        // 根据参数aid，调用deleteByAid()执行删除
        Integer rows = cartMapper.deleteById(id);
        if (rows != 1) {
            throw new DeleteException("删除数据时出现未知错误，请联系系统管理员");
        }
    }


    @Override
    public int deleteAll(Integer[]ids) {
        //判定
        if (ids==null || ids.length==0)
            throw new CartNotFoundException("请选择购物车数据");
        //执行删除操作
        int rows;
        try {
            rows = cartMapper.deleteByIds(ids);
        }catch(Throwable e) {
            e.printStackTrace();
            //发出报警信息(例如给运维人员发短信)

            throw new DeleteException("删除数据时出现未知错误，请联系系统管理员");
        }
        //对结果进行验证
        if (rows==0)
            throw new CartNotFoundException("尝试访问的购物车数据不存在");
        return rows;
    }




}
