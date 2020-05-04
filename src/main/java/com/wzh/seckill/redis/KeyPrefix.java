package com.wzh.seckill.redis;

/**
 * Created by admin on 2020/5/4.
 */
public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
