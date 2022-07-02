package com.iflytek.web.mapper.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Recommendation {
    private Integer id;
    private Integer userid;
    private Date createtime;
    private String list;
}
