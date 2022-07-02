package com.iflytek.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.web.mapper.GoodsMapper;
import com.iflytek.web.pojo.AlsoBuy;
import com.iflytek.web.mapper.pojo.Evaluate;
import com.iflytek.web.pojo.Goods;
import com.iflytek.web.pojo.Recommendation;
import com.iflytek.web.viewmodel.Goods4List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private AlsoBuyService alsoBuyService;

    public List<Goods> hotGoods(){
        return goodsMapper.queryHotGoods();
    }

    public Goods4List getGoods4ListById(int goodsId){
        Goods item = this.getById(goodsId);
        Goods4List result = new Goods4List();
        result.setId(item.getId());
        result.setName(item.getGoodsName());
        result.setPrice(item.getGoodsPrice());
        result.setUrl(item.getUrl());
        result.setDescription(item.getGoodsIntroduce());
        result.setCategoryId(item.getCategoryId());
        result.setCategoryId(item.getCategoryId());
        String slide=item.getSlidePicture();

        //处理华东图片
        slide=slide.substring(1, slide.length()-1);
        String[] sp = slide.split("\\|");
        result.setSlide_1(sp[0].substring(1, sp[0].length()-1));
        result.setSlide_2(sp[1].substring(1, sp[1].length()-1));
        result.setSlide_3(sp[2].substring(1, sp[2].length()-1));
        result.setSlide_4(sp[3].substring(1, sp[3].length()-1));
        //处理detail图片
        if (item.getDetailPicture()!=null) {
            String details = item.getDetailPicture();

            List<String> sList=new ArrayList<>();
            if (details.length()>2) {
                details = details.substring(1, details.length() - 1);
                String[] sDetails = details.split("\\|");
                for (String s : sDetails) {
                    sList.add(s.substring(1, s.length()-1));
                }
            }
            result.setDetailPicture(sList);
        }

        return result;
    }

    public List<Goods> queryRecommendation(Integer userId){
        QueryWrapper<Recommendation> queryRcdtWrapper = new QueryWrapper<>();
        queryRcdtWrapper.eq("userid", userId);
        Recommendation rcdt = recommendationService.getOne(queryRcdtWrapper);//查询一条记录
        // select g.* from goods g where g.id in (select list from recommendation where userid=userId)
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", rcdt.getList());
        List<Goods> list = this.list(queryWrapper);
        return list;
    }

    public List<Goods> queryAlsoByRecommendation(Integer goodsId){
        QueryWrapper<AlsoBuy> queryRcdtWrapper = new QueryWrapper<>();
        queryRcdtWrapper.eq("goodsid", goodsId);
        AlsoBuy rcdt = alsoBuyService.getOne(queryRcdtWrapper);
        // select g.* from goods g where g.id in (select list from recommendation where userid=userId)
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", rcdt.getList());
        List<Goods> list = this.list(queryWrapper);
        return list;
    }
    public int addEvaluation(String goodsId, String evaluation, String username) {
        Evaluate evaluate =  Evaluate.builder()
                .goodsId(Long.valueOf(goodsId))
                .eval(evaluation)
                .createTime(new Date())
                .username(username).build();
        return goodsMapper.addEvaluation(evaluate);
    }
}
