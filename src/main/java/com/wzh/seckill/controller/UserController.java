package com.wzh.seckill.controller;

import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.result.Result;
import com.wzh.seckill.service.GoodsService;
import com.wzh.seckill.service.SeckillUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2020/5/8.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;


    @RequestMapping("/info")
    @ResponseBody
    public Result<SeckillUser> info(Model model, SeckillUser user) {
        return Result.success(user);

    }
}
