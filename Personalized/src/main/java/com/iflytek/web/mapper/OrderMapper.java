package com.iflytek.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.web.pojo.Order;
import com.iflytek.web.viewmodel.OrderModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    public List<OrderModel> queryOrderByUserId(Integer userId);
}
