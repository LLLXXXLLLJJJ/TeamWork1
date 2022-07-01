package com.iflytek.web;

import com.iflytek.web.service.CartService;
import com.iflytek.web.service.OrderService;
import com.iflytek.web.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Test
    public void testQueryOrderByUserId(){
        Integer userId = 11;
        orderService.queryOrderByUserId(userId).stream().forEach(p-> System.out.println(p));

    }

}
