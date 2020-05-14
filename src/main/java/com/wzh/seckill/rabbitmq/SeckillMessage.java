package com.wzh.seckill.rabbitmq;

import com.wzh.seckill.domain.SeckillUser;

/**
 * Created by admin on 2020/5/13.
 */
public class SeckillMessage {
    private SeckillUser user;
    private long goodsId;

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
