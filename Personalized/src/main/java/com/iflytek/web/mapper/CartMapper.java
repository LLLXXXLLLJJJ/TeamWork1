package com.iflytek.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.web.pojo.GoodsCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<GoodsCart> {

    public List<GoodsCart> getByUserId(Integer userId);
}
