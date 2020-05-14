package com.wzh.seckill.rabbitmq;

import com.wzh.seckill.domain.OrderInfo;
import com.wzh.seckill.domain.SeckillOrder;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.result.CodeMsg;
import com.wzh.seckill.result.Result;
import com.wzh.seckill.service.GoodsService;
import com.wzh.seckill.service.OrderService;
import com.wzh.seckill.service.SeckillService;
import com.wzh.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by admin on 2020/5/13.
 */
@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * Direct模式 交换机Exchange
     *
     * @param message
     */
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message:" + message);
//
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info("topic queue1 message:" + message);
//
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info("topic queue2 message:" + message);
//
//    }
//
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info("header queue message:" + new String(message));
//    }

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        SeckillMessage sm=RedisService.stringToBean(message,SeckillMessage.class);
        SeckillUser user=sm.getUser();
        long goodsId=sm.getGoodsId();

        //判断库存
        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goods.getStockCount();
        if (stock<=0){

            return ;
        }


        //判断是否已经秒杀到了
        SeckillOrder order =orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order!=null){

            return ;
        }

        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo =seckillService.seckill(user,goods);




    }
}
