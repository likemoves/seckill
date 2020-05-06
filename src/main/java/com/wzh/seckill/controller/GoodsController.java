package com.wzh.seckill.controller;

import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping("/to_list")
    public String toList(Model model, SeckillUser user) {

        model.addAttribute("user", user);
        return "goods_list";
    }

//    @RequestMapping("/to_detail")
//    public String toDetial(HttpServletResponse response,Model model,
//                          @CookieValue(value=SeckillUserService.COOK_NAME_TOKEN,required = false) String cookieToken,
//                          @RequestParam(value=SeckillUserService.COOK_NAME_TOKEN,required = false) String paramToken) {
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return "login";
//
//        }
//        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        SeckillUser user=seckillUserService.getByToken(response,token);
//        model.addAttribute("user",user);
//        return "goods_list";
//    }
}
