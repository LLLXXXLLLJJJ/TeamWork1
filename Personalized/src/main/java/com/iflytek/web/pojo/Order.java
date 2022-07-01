package com.iflytek.web.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("order_info")
public class Order {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String orderNo;
    private Double money;
    private Byte status;
    private Integer userId;
    private Date createTime;
}
