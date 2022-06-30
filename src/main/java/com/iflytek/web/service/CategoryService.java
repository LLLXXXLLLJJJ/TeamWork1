package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.CategoryMapper;
import com.iflytek.web.pojo.Category;
import com.iflytek.web.pojo.GroupCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    @Autowired
    private CategoryMapper categoryMapper;
    public List<Category> queryAll(){
        List<Category> categoryList = categoryMapper.selectList(null);
        List<Category> parentCategory  = categoryList.stream().filter(item->item.getParentId() == null).collect(Collectors.toList());
        categoryList.removeAll(parentCategory); // 剩下的就不是父元素

        // categoryList 除掉根节点的所有元素   parentCategory 对根节点进行遍历，找到根节点下所有的分类
        parentCategory.forEach(parent->{
            int parentId = parent.getId();
            // 找到根节点下所有的分类
            List<Category> childCategoryList = categoryList.stream().filter(item -> item.getParentId() == parentId).collect(Collectors.toList());

            // 分组
            List<GroupCategory> groupCategoryList = new ArrayList<>();
            Map<String, GroupCategory> map = new HashMap<>();
            childCategoryList.stream().forEach(item -> {
                if (!map.containsKey(item.getTerm())){
                    GroupCategory groupCategory = new GroupCategory();
                    List<Category> childList = new ArrayList<>();
                    groupCategory.setCategoryList(childList);
                    groupCategory.setGroupName(item.getTerm());
                    groupCategory.setParentId(parentId);
                    childList.add(item);
                    map.put(item.getTerm(), groupCategory);
                    groupCategoryList.add(groupCategory);
                }else {
                    GroupCategory groupCategory = map.get(item.getTerm());
                    groupCategory.getCategoryList().add(item);
                }
            });
            parent.setGroups(groupCategoryList);
        });

        return parentCategory;
    }
}
