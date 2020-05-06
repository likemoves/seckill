package com.wzh.seckill.dao;

import com.wzh.seckill.domain.Goods;
import com.wzh.seckill.domain.SeckillGoods;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by admin on 2020/5/5.
 */
@Mapper
@Repository
public interface GoodsDao {
    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.seckill_price from seckill_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.seckill_price from seckill_goods mg left join goods g on mg.goods_id=g.id where g.id=#{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId );

    @Update("update seckill_goods set stock_count=stock_count-1 where goods_id=#{goodsId}")
    public int reduceStock(SeckillGoods g);
}