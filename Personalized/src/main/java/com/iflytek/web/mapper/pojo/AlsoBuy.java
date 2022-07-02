package com.iflytek.web.mapper.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("alsobuy")
public class AlsoBuy {
    private Integer id;
    private Integer goodsid;
    private String list;
}
