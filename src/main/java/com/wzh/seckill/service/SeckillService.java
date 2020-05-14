package com.wzh.seckill.service;

import com.wzh.seckill.dao.GoodsDao;
import com.wzh.seckill.domain.Goods;
import com.wzh.seckill.domain.OrderInfo;
import com.wzh.seckill.domain.SeckillOrder;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.redis.SeckillKey;
import com.wzh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin on 2020/5/6.
 */
@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods) {

        //减库存 下订单 写入秒杀订单
        boolean success=goodsService.reduceStock(goods);
        if (success){
            return orderService.createOrder(user,goods);
        }else {
            //在缓存redis中设置一个标识
            setGoodsOver(goods.getId());
            return null;
        }

    }



    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order=orderService.getSeckillOrderByUserIdGoodsId(userId,goodsId);
        if (order!=null){
            return order.getOrderId();
        }else {
            boolean isOver=getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {

        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true);

    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }


    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }
}
