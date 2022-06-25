package com.iflytek.web.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GroupCategory {
    private Integer parentId;
    private String groupName;
    private List<Category> categoryList;
}
