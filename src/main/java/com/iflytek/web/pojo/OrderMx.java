package com.iflytek.web.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class OrderMx {
    private Integer id;
    private Integer orderId;
    private String orderNo;
    private Integer goodsId;
    private Integer num;
    private Date createTime;
    private Integer creator;

}
