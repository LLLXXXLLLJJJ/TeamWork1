package com.iflytek.web.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class OrderMx {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private String orderNo;
    private Integer goodsId;
    private Integer num;
    private Date createTime;
    private Integer creator;

}
