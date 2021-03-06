package com.wzh.seckill.dao;

import com.wzh.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by admin on 2020/5/3.
 */
@Mapper
@Repository
public interface UserDao {

    @Select("select * from user where id=#{id}")
    public User getById(@Param("id") int id);


    @Insert("insert into user(id,name) values(#{id}, #{name})")
    public int insert(User user);


}
