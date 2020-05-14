package com.wzh.seckill.service;

import com.wzh.seckill.dao.GoodsDao;
import com.wzh.seckill.dao.OrderDao;
import com.wzh.seckill.domain.OrderInfo;
import com.wzh.seckill.domain.SeckillOrder;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.OrderKey;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;



/**
 * Created by admin on 2020/5/6.
 */
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {

        //查询缓存
        return redisService.get(OrderKey.getSeckillOrderByUserIdGoodsId,""+userId+"_"+goodsId,SeckillOrder.class);


        //查询数据库
       // return orderDao.getSeckillOrderByUserIdGoodsId(userId, goodsId);
    }

    @Transactional
    public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);

        //建立唯一索引 防止同一个用户写入多条数据  即每个用户只能秒杀一次
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderDao.insertSeckillOrder(seckillOrder);

        //生成缓存
        redisService.set(OrderKey.getSeckillOrderByUserIdGoodsId,""+user.getId()+"_"+goods.getId(),seckillOrder);

        return orderInfo;


    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }


    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteMiaoshaOrders();
    }
}
