package com.iflytek.web.viewmodel;

import com.iflytek.web.pojo.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Goods4List {
    private int id;
    private String name;
    private long price;
    private String url;
    private String description;
    private int categoryId;
    private String slide_1;
    private String slide_2;
    private String slide_3;
    private String slide_4;
    private Category category;
    private List<String> detailPicture;
}
