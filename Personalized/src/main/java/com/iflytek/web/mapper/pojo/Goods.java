package com.iflytek.web.mapper.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 数据表Goods映射类
 */

@Data
@ToString
public class Goods {
    private Integer id;
    private String goodsName;
    private String goodsIntroduce;
    private double goodsPrice;
    private Integer categoryId;
    private String url;
    private String slidePicture;
    private double grade;
    private Integer rankNum;
    private String detailPicture;
}