package com.iflytek.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iflytek.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 入行  -> 跳

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // 认证的过程
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username); // username页面来的   select * from login where username= ? and password = ?
      // queryWrapper.eq("password","$10$ujas02psjl3fPoTD4zEoe.4d.gistfuGnVjS/w0Hl6BBbHofoG872" );
        User user = userService.getOne(queryWrapper);
        //userService.
        if (user == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        }
        Collection<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
       SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER"); // 这里设置的是权限
       grantedAuthorities.add(authority);
      user.setAuthorities(grantedAuthorities);
        return user;
    }
}
