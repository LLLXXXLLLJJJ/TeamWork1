package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.UserMapper;
import com.iflytek.web.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
