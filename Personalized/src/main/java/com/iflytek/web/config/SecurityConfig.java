/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iflytek.web.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.web.pojo.Recommendation;
import com.iflytek.web.pojo.User;
import com.iflytek.web.util.PythonUtil;
import com.iflytek.web.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private RecommendationService recommendationService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests((authorize) -> authorize
						.antMatchers("/user/check/**","/user/register","/css/**", "/home","/home/**","/","/js/**","/images/**","/fonts/**").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin((formLogin) -> formLogin
								.loginPage("/login")
								.loginProcessingUrl("/doLogin")
								.failureUrl("/login")
								.permitAll()
//                        .successForwardUrl("/index")
								.successHandler(new AuthenticationSuccessHandler() {
									@Override
									public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
										// 放到session里面去
										//获取头部信息
										SecurityContext context = SecurityContextHolder.createEmptyContext();
										context.setAuthentication(authentication);
										SecurityContextHolder.setContext(context);

										User obj = (User)authentication.getPrincipal();
										String xRequestedWith=httpServletRequest.getHeader("X-Requested-With");
										if(xRequestedWith!= null && xRequestedWith.indexOf("XMLHttpRequest")!=-1){
											// 是ajax的处理方式
											httpServletRequest.getSession().setAttribute("Id", obj.getId());
											httpServletRequest.getSession().setAttribute("userName", obj.getUserName());

											// 向页面上输入true或者false
											httpServletResponse.setContentType("application/json;charset=utf-8");
											ServletOutputStream out = httpServletResponse.getOutputStream();
											ObjectMapper objectMapper = new ObjectMapper();
											objectMapper.writeValue(out, true);
											out.flush();
											out.close();
										}else{

											httpServletRequest.getSession().setAttribute("user", obj);
											httpServletRequest.getSession().setAttribute("userName", obj.getUserName());
											httpServletResponse.sendRedirect("/home/");
										}

										try {
											// 查询数据库 个性化推荐表，如果存在， 判断时间是否为今天，如果为今天不执行下面的操作，否则执行更新个性化推荐的商品
											QueryWrapper<Recommendation> queryWrapper = new QueryWrapper<>();
											queryWrapper.eq("Id", obj.getId());
											Recommendation recommendation = recommendationService.getOne(queryWrapper);
											// 个性化推荐没有做，那么就推荐； 或者 昨天之前推荐的，而今天没有推荐，那么也进行推荐
											if (recommendation == null || recommendation.getCreatetime().before(new Date(System.currentTimeMillis() - 24*60*60*1000L))){
												PythonUtil.executePython(obj.getId(), PythonUtil.PERSONAL_RECOMMENDATION_FIILE);
											}
										}catch (Exception e){
											e.printStackTrace();
										}
									}
								})
								.failureHandler(new AuthenticationFailureHandler() {
									@Override
									public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
										// 登录失败要返回错误消息到页面上
										httpServletRequest.setAttribute("error", e.getMessage());

										String xRequestedWith=httpServletRequest.getHeader("X-Requested-With");
										if(xRequestedWith!= null && xRequestedWith.indexOf("XMLHttpRequest")!=-1){
											// 向页面上输入true或者false
											httpServletResponse.setContentType("application/json;charset=utf-8");
											ServletOutputStream out = httpServletResponse.getOutputStream();
											ObjectMapper objectMapper = new ObjectMapper();
											objectMapper.writeValue(out, false);
											out.flush();
											out.close();
										}else{
											httpServletResponse.sendRedirect("/login");
										}
									}
								})
								//.defaultSuccessUrl("/home/",true) // 登录成功重定向
				)
//				.headers(headers -> headers
//						.frameOptions(frameOptions -> frameOptions.sameOrigin())   // 处理iframe
//				)
				.logout((logout) ->
						logout
								.permitAll()
				)
				.csrf(csrf -> csrf.disable());
	}

	@Bean
	public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder(){
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return super.matches(rawPassword, encodedPassword);
			}
		};
	}
}
