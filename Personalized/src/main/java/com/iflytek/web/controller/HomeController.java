package com.iflytek.web.controller;

import com.iflytek.web.pojo.Category;
import com.iflytek.web.pojo.Goods;
import com.iflytek.web.pojo.User;
import com.iflytek.web.service.CategoryService;
import com.iflytek.web.service.GoodsService;
import com.iflytek.web.viewmodel.Goods4List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@SessionAttributes(value = {"categories","hotGoods", "personalGoods"})
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 系统首页
     * @param model ViewModel
     * @return
     */
    @RequestMapping("")
    public String Index(@AuthenticationPrincipal User user, Model model, HttpSession httpSession) {
        List<Category> list = categoryService.queryAll();
        List<Goods> hotGoodsList = goodsService.hotGoods();

        model.addAttribute("categories", list);
        model.addAttribute("hotGoods",hotGoodsList);

        if (user != null){
            List<Goods> personalGoods = goodsService.queryRecommendation(user.getId());
            model.addAttribute("personalGoods", personalGoods);
        }else {
            model.addAttribute("personalGoods", hotGoodsList);
        }
        return "index";
    }

    /**
     *物品详情页
     * @param model
     * @param goodsId
     * @return
     */
    @RequestMapping("/productView/{gId}")
    public String ProductView(Model model, @PathVariable("gId") int goodsId, HttpSession httpSession){
        Goods4List goods = goodsService.getGoods4ListById(goodsId);
        // 调用业务对象执行获取数据 Product data = productService.findById(id);
        model.addAttribute("goods", goods);

        List<Goods> alsoBuyGoodsList = goodsService.queryAlsoByRecommendation(goodsId);
        model.addAttribute("alsobuyGoods", alsoBuyGoodsList);
        return "detail";
    }



}
