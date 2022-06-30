package com.iflytek.web;


import com.iflytek.web.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    public void testGetByUserId(){
        Integer userId = 1;
        cartService.getByUserId(userId).stream().forEach(p -> System.out.println(p));
        System.out.println(cartService.getById(11));
    }
}
