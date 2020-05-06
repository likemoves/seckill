package com.wzh.seckill.controller;

import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.service.GoodsService;
import com.wzh.seckill.service.SeckillUserService;
import com.wzh.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by admin on 2020/5/5.
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService  goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model, SeckillUser user) {

        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
          return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetial(Model model, SeckillUser user, @PathVariable("goodsId")long goodsId) {
        //snowflake
        model.addAttribute("user",user);
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);


        long startAt=goods.getStartDate().getTime();
        long endAt=goods.getEndDate().getTime();
        long now=System.currentTimeMillis();

        int seckillStatus=0;
        int remainSeconds=0;
        if (now<startAt){
            //秒杀还没开始，倒计时
            seckillStatus=0;
            remainSeconds= (int) ((startAt-now)/1000);
        }else if(now>endAt){
            //秒杀已经结束
            seckillStatus=2;
            remainSeconds=-1;
        }else {
            //秒杀进行中
            seckillStatus=1;
            remainSeconds=0;
        }

        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";
    }
}
