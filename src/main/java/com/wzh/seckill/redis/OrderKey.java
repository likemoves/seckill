package com.wzh.seckill.redis;

/**
 * Created by admin on 2020/5/12.
 */
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getSeckillOrderByUserIdGoodsId=new OrderKey("soug");

}
