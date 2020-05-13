package com.wzh.seckill.controller;

import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.GoodsKey;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.result.Result;
import com.wzh.seckill.service.GoodsService;
import com.wzh.seckill.service.SeckillUserService;
import com.wzh.seckill.vo.GoodsDetailVo;
import com.wzh.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user) {

        model.addAttribute("user", user);
        //页面缓存
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //  return "goods_list";



        SpringWebContext springWebContext=new SpringWebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap(),applicationContext);
        //手动渲染
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",springWebContext);
        if(!StringUtils.isEmpty(html)){
            //所有人访问同一个页面
           redisService.set(GoodsKey.getGoodsList,"",html);
        }

        return html;
    }

    @RequestMapping(value ="/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetial2(HttpServletRequest request, HttpServletResponse response,Model model,
                           SeckillUser user, @PathVariable("goodsId")long goodsId) {
        //snowflake
        model.addAttribute("user",user);

        //页面缓存
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsDetail,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //手动渲染
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

//        return "goods_detail";


        SpringWebContext springWebContext=new SpringWebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap(),applicationContext);
        //手动渲染
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",springWebContext);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }


    @RequestMapping(value ="/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetial(HttpServletRequest request, HttpServletResponse response, Model model,
                                           SeckillUser user, @PathVariable("goodsId")long goodsId) {


        //手动渲染
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);


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

        GoodsDetailVo goodsDetailVo=new GoodsDetailVo();
        goodsDetailVo.setUser(user);
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setSeckillStatus(seckillStatus);



        return Result.success(goodsDetailVo);
    }
}
