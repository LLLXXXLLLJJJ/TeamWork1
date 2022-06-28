package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.CartMapper;
import com.iflytek.web.mapper.UserMapper;
import com.iflytek.web.pojo.GoodsCart;
import com.iflytek.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    /**
     * 判断用户名是否存在
     * */
    public boolean userNameExists(String username){
        boolean result=true;

        List<User> list=userMapper.getUserInfoByUserName(username);
        if(list==null || list.size()==0) {
            result=false;
        }

        return result;
    }

    /**
     * 添加新用户
     * */
    public boolean addUser(String userName,String passWord){
        boolean result=false;

        try{
            userMapper.addUser(userName,passWord);
            result=true;
        }catch (Exception e)
        {e.printStackTrace();
        }


        return result;
    }

}
