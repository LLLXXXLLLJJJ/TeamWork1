package com.iflytek.web.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 对应菜单分类
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;
    private String title;
    private String term;
    private Integer parentId;  // parent_id

    @TableField(exist = false)
    private List<GroupCategory> groups;
}
