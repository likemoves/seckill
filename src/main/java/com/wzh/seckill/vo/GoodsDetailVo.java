package com.wzh.seckill.vo;

import com.wzh.seckill.domain.Goods;
import com.wzh.seckill.domain.SeckillUser;

import java.util.Date;

/**
 * Created by admin on 2020/5/6.
 */
public class GoodsDetailVo extends Goods{

    private int seckillStatus=0;
    private int remainSeconds=0;
    private GoodsVo goods;
    private SeckillUser user;

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public void setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
    }

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    public int getRemainSeconds() {

        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoodsVo() {
        return goods;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goods = goodsVo;
    }
}
