package com.iflytek.web.vo;

import lombok.Data;

@Data
public class R {
    private Integer code;
    private String message;
    private Object data;

    public static R SUCCESS(){
        R r = new R();
        r.setCode(0);
        r.setMessage("成功");
        return r;
    }

    public static R ERROR(){
        R r = new R();
        r.setCode(1);
        r.setMessage("失败");
        return r;
    }
}
