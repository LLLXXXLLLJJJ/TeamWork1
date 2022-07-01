package com.iflytek.web.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName(value = "goodscart")
public class GoodsCart implements Serializable {
    private Integer id;
    private Integer userId;  // user_id
    private Integer number;
    private Integer goodsId; // goods_id
    private Date createTime; // create_time
    private Integer status;
    private Long goodsPrice;

    @TableField(exist = false)
    private Goods goods;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoodsCart)) return false;

        GoodsCart goodsCart = (GoodsCart) o;

        if (getId() != null ? !getId().equals(goodsCart.getId()) : goodsCart.getId() != null) return false;
        if (getUserId() != null ? !getUserId().equals(goodsCart.getUserId()) : goodsCart.getUserId() != null) return false;
        if (getGoodsId() != null ? !getGoodsId().equals(goodsCart.getGoodsId()) : goodsCart.getGoodsId() != null) return false;
        if (getGoodsPrice() != null ? !getGoodsPrice().equals(goodsCart.getGoodsPrice()) : goodsCart.getGoodsPrice() != null) return false;
        return getNumber() != null ? getNumber().equals(goodsCart.getNumber()) : goodsCart.getNumber() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = 31 * result + (getGoodsId() != null ? getGoodsId().hashCode() : 0);
        result = 31 * result + (getGoodsPrice() != null ? getGoodsPrice().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        return result;
    }

    /**
     * @Override
     *     public boolean equals(Object o) {
     *         if (this == o) return true;
     *         if (!(o instanceof Cart)) return false;
     *
     *         Cart cart = (Cart) o;
     *
     *         if (getCid() != null ? !getCid().equals(cart.getCid()) : cart.getCid() != null) return false;
     *         if (getUid() != null ? !getUid().equals(cart.getUid()) : cart.getUid() != null) return false;
     *         if (getPid() != null ? !getPid().equals(cart.getPid()) : cart.getPid() != null) return false;
     *         if (getPrice() != null ? !getPrice().equals(cart.getPrice()) : cart.getPrice() != null) return false;
     *         return getNum() != null ? getNum().equals(cart.getNum()) : cart.getNum() == null;
     *     }
     *
     *     @Override
     *     public int hashCode() {
     *         int result = getCid() != null ? getCid().hashCode() : 0;
     *         result = 31 * result + (getUid() != null ? getUid().hashCode() : 0);
     *         result = 31 * result + (getPid() != null ? getPid().hashCode() : 0);
     *         result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
     *         result = 31 * result + (getNum() != null ? getNum().hashCode() : 0);
     *         return result;
     *     }
     * */
}
