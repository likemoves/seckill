package com.wzh.seckill.controller;

import com.wzh.seckill.domain.User;
import com.wzh.seckill.rabbitmq.MQSender;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.redis.UserKey;
import com.wzh.seckill.result.Result;
import com.wzh.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2020/5/3.
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "jack");
        return String.valueOf(Result.success("hello,world"));
    }

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//        mqSender.send("hello rabbitMQ");
//        return Result.success("hello,world");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic() {
//        mqSender.sendTopic("hello rabbitMQ");
//        return Result.success("hello,world");
//    }
//
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout() {
//        mqSender.sendFanout("hello rabbitMQ");
//        return Result.success("hello,world");
//    }
//
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header() {
//        mqSender.sendHeader("hello rabbitMQ");
//        return Result.success("hello,world");
//    }



    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user=userService.getById(1);
        return Result.success(user);

    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
       userService.tx();
        return Result.success(true);

    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
       User user=redisService.get(UserKey.getById,""+1,User.class);
       return Result.success(user);

    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);

    }
}
