package com.wzh.seckill.controller;

import com.wzh.seckill.domain.OrderInfo;
import com.wzh.seckill.domain.SeckillOrder;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.rabbitmq.MQSender;
import com.wzh.seckill.rabbitmq.SeckillMessage;
import com.wzh.seckill.redis.GoodsKey;
import com.wzh.seckill.redis.OrderKey;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.redis.SeckillKey;
import com.wzh.seckill.result.CodeMsg;
import com.wzh.seckill.result.Result;
import com.wzh.seckill.service.GoodsService;
import com.wzh.seckill.service.OrderService;
import com.wzh.seckill.service.SeckillService;
import com.wzh.seckill.service.SeckillUserService;
import com.wzh.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2020/5/6.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean{
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

    @Autowired
    MQSender sender;

    private Map<Long,Boolean> localOverMap= new HashMap<Long,Boolean>();

    /**
     *
     *GET POST区别
     */
    @RequestMapping(value = "/do_seckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> toList(Model model, SeckillUser user, @RequestParam("goodsId")long goodsId) {
        if (user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //内存标记，减少redis访问
        boolean over=localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.SECKILL_OVER);
        }


        //预减库存
        long stock=redisService.decr(GoodsKey.getSeckillGoodsStock,""+goodsId);
        if (stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //判断是否已经秒杀到了
        SeckillOrder order =orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order!=null){

            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        //入队
        SeckillMessage sm=new SeckillMessage();
        sm.setUser(user);
        sm.setGoodsId(goodsId);
        sender.sendSeckillMessage(sm);
        return Result.success(0);//排队中



        /*
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
        */

    }

    /**
     * 系统初始化时的操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        if (goodsList==null){
            return;
        }
        for (GoodsVo goods:goodsList){
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }

    }


    /**
     * orderId:成功
     * -1：秒杀失败
     * 0：排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, SeckillUser user, @RequestParam("goodsId")long goodsId) {
        long result=seckillService.getSeckillResult(user.getId(),goodsId);
        return Result.success(result);

    }


    /**
     * 重置系统
     * @param model
     * @return
     */
    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getSeckillGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getSeckillOrderByUserIdGoodsId);
        redisService.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsList);
        return Result.success(true);
    }


}
