package com.wzh.seckill.vo;

import com.wzh.seckill.domain.OrderInfo;

/**
 * Created by admin on 2020/5/12.
 */
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
