package com.wzh.seckill.service;

import com.wzh.seckill.dao.GoodsDao;
import com.wzh.seckill.domain.Goods;
import com.wzh.seckill.domain.SeckillGoods;
import com.wzh.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2020/5/6.
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g=new SeckillGoods();
        g.setGoodsId(goods.getId());

        int ret=goodsDao.reduceStock(g);
        return ret>0;
    }


    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            SeckillGoods g = new SeckillGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }

}
