package com.iflytek.web.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/** 购物车数据的Value Object类 */
@Data
@ToString
public class CartVO implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private Integer number;
    private Long goodsPrice;
    private String goodsIntroduce;
    private String url;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartVO)) return false;

        CartVO cartVO = (CartVO) o;

        if (getId() != null ? !getId().equals(cartVO.getId()) : cartVO.getId() != null) return false;
        if (getUserId() != null ? !getUserId().equals(cartVO.getUserId()) : cartVO.getUserId() != null) return false;
        if (getGoodsId() != null ? !getGoodsId().equals(cartVO.getGoodsId()) : cartVO.getGoodsId() != null) return false;
        if (getGoodsPrice() != null ? !getGoodsPrice().equals(cartVO.getGoodsPrice()) : cartVO.getGoodsPrice() != null) return false;
        if (getNumber() != null ? !getNumber().equals(cartVO.getNumber()) : cartVO.getNumber() != null) return false;
        if (getGoodsIntroduce() != null ? !getGoodsIntroduce().equals(cartVO.getGoodsIntroduce()) : cartVO.getGoodsIntroduce() != null) return false;
        return getUrl() != null ? getUrl().equals(cartVO.getUrl()) : cartVO.getUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = 31 * result + (getGoodsId() != null ? getGoodsId().hashCode() : 0);
        result = 31 * result + (getGoodsPrice() != null ? getGoodsPrice().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getGoodsIntroduce() != null ? getGoodsIntroduce().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        return result;
    }

}
