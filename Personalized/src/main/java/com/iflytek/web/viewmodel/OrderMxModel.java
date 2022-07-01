package com.iflytek.web.viewmodel;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderMxModel {
    private Integer id;
    private Integer goodsId;
    private Integer num;
    private Double goodsPrice;
    private String goodsName;
    private String url;
    private Integer categoryId;

}
