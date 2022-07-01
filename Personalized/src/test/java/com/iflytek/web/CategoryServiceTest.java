package com.iflytek.web;

import com.iflytek.web.pojo.Category;
import com.iflytek.web.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testQueryAll(){
        List<Category> list = categoryService.queryAll();
        list.forEach(p->p.getGroups().forEach(item-> System.out.println(item)));
        System.out.println(list.size());
    }

}
