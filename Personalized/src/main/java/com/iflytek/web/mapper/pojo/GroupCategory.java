package com.iflytek.web.mapper.pojo;

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
