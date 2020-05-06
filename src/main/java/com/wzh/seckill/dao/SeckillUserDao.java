package com.wzh.seckill.dao;

import com.wzh.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by admin on 2020/5/5.
 */
@Mapper
@Repository
public interface SeckillUserDao {
    @Select("select * from seckill_user where id = #{id}")
    public SeckillUser getById(@Param("id")long id);

    @Update("update seckill_user set password = #{password} where id = #{id}")
    public void update(SeckillUser toBeUpdate);
}
