package com.wzh.seckill.redis;

/**
 * Created by admin on 2020/5/11.
 */
public class GoodsKey extends BasePrefix {
    public GoodsKey(String prefix) {
        super(prefix);
    }

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList=new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail=new GoodsKey(60,"gd");
}
