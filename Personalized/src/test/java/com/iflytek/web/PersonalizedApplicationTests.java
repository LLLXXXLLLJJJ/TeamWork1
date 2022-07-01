package com.iflytek.web;

import com.iflytek.web.pojo.Goods;
import com.iflytek.web.service.GoodsService;
import com.iflytek.web.viewmodel.Goods4List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonalizedApplicationTests {

//
//    @Autowired
//    CategoryService categoryService;
//
//    @Test
//    public void contextLoads() {
//    }
//
//    @Test
//    public void testQueryAll(){
//        List<Category> list = categoryService.queryAll();
//        list.forEach(p->p.getGroups().forEach(item-> System.out.println(item)));
//        System.out.println(list.size());
//    }

    @Autowired
    GoodsService goodsService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testHotGoods(){
        List<Goods> list = goodsService.hotGoods();
        list.forEach(p-> System.out.println(p));
    }

    @Test
    public void testGetGoods4ListById(){
        int goodsId = 3465001;
        Goods4List goods =  goodsService.getGoods4ListById(goodsId);
        System.out.println(goods);
    }

}
