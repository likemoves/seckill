package com.wzh.seckill.util;

import java.util.UUID;

/**
 * Created by admin on 2020/5/5.
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
