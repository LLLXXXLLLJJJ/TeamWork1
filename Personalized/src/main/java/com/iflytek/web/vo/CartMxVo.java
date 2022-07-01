package com.iflytek.web.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CartMxVo {
    private Integer num;
    private Integer cartId;
    private Integer goodsId;
    private double price;

}
