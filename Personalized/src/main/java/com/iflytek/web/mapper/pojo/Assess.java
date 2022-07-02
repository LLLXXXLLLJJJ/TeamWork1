package com.iflytek.web.mapper.pojo;
import java.util.Date;

@lombok.Data
public class Assess {
    private Integer id;
    private Integer userid;
    private Integer goodsid;
    private Integer cartid;
    private Double grade;
    private String comment;
    private Date createtime;


}
