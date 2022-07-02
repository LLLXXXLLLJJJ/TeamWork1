package com.iflytek.web.mapper.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author linqingfeng
 * @date 2022/7/1 21:50
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evaluate {
    private Long id;
    private Long goodsId;
    private String eval;
    private Date createTime;
    private Date updateTime;
    private String scorce;
    private String username;
}
