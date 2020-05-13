package com.wzh.seckill.controller;

import com.wzh.seckill.domain.OrderInfo;
import com.wzh.seckill.domain.SeckillOrder;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.result.CodeMsg;
import com.wzh.seckill.result.Result;
import com.wzh.seckill.service.GoodsService;
import com.wzh.seckill.service.OrderService;
import com.wzh.seckill.service.SeckillService;
import com.wzh.seckill.service.SeckillUserService;
import com.wzh.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by admin on 2020/5/6.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    /**
     *
     *GET POST区别
     */
    @RequestMapping(value = "/do_seckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> toList(Model model, SeckillUser user, @RequestParam("goodsId")long goodsId) {
        if (user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goods.getStockCount();
        if (stock<=0){

            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //判断是否已经秒杀到了
        SeckillOrder order =orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order!=null){

            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo =seckillService.seckill(user,goods);


        return Result.success(orderInfo);
    }
}
