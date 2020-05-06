package com.wzh.seckill.redis;

/**
 * Created by admin on 2020/5/5.
 */
public class SeckillUserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE=3600*24*2;
    public SeckillUserKey(String prefix) {
        super(prefix);
    }

    public SeckillUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SeckillUserKey token=new SeckillUserKey(TOKEN_EXPIRE,"tk");
}
