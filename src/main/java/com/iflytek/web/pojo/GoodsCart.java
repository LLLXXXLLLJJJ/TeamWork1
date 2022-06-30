package com.iflytek.web.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName(value = "goodscart")
public class GoodsCart {
    private Integer id;
    private Integer userId;  // user_id
    private Integer number;
    private Integer goodsId; // goods_id
    private Date createTime; // create_time
    private Integer status;

    @TableField(exist = false)
    private Goods goods;
}
