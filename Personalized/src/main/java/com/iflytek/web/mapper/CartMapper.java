package com.iflytek.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.vo.CartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
//    public interface CartMapper extends BaseMapper<GoodsCart> {
//    public List<GoodsCart> getByUserId(Integer userId);


    int insert (GoodsCart goodsCart);


    Integer updateNumById(
            @Param("id") Integer id,
            @Param("number") Integer number);



    GoodsCart findByUserIdAndGoodsId(
            @Param("userId") Integer userId,
            @Param("goodsId") Integer goodsId);


    //List<CartVO> findVOByUserId(Integer userId);
    List<GoodsCart> getByUserId(Integer userId);


    GoodsCart findById(Integer id);


    //List<CartVO> findVOByIds(Integer[] ids);
    List<GoodsCart> findVOByIds(Integer[] ids);


     Integer deleteById(Integer id);


    Integer deleteByIds(@Param("ids")Integer[]ids);
}
