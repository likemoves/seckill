package com.wzh.seckill.redis;

/**
 * Created by admin on 2020/5/11.
 */
public class SeckillKey extends BasePrefix {
    public SeckillKey(String prefix) {
        super(prefix);
    }

    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SeckillKey isGoodsOver=new SeckillKey(0,"go");

}
