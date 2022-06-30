package com.iflytek.web.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.RecommendationMapper;
import com.iflytek.web.pojo.Recommendation;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService extends ServiceImpl<RecommendationMapper, Recommendation> {
}
