package com.iflytek.web.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString

public class Order {
    private Integer id;
    private String orderNo;
    private Double money;
    private Byte status;
    private Integer userId;
    private Date createTime;



}
