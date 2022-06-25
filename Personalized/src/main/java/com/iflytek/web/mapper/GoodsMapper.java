package com.iflytek.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.web.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    public List<Goods> queryHotGoods();
}
