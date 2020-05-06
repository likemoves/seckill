package com.wzh.seckill.service;

import com.sun.deploy.net.HttpResponse;

import com.wzh.seckill.dao.SeckillUserDao;
import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.exception.GlobleException;
import com.wzh.seckill.redis.RedisService;
import com.wzh.seckill.redis.SeckillUserKey;
import com.wzh.seckill.result.CodeMsg;
import com.wzh.seckill.util.MD5Util;
import com.wzh.seckill.util.UUIDUtil;
import com.wzh.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2020/5/5.
 */
@Service
public class SeckillUserService {

    public static final String COOK_NAME_TOKEN="token";

    @Autowired
    SeckillUserDao seckillUserDao;

    @Autowired
    RedisService redisService;

    public SeckillUser getById(long id) {
        return seckillUserDao.getById(id);

    }

    public boolean login(HttpServletResponse response,LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        SeckillUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!dbPass.equals(calcPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token= UUIDUtil.uuid();
        addCookie(response,token,user);

        return true;


    }

    public SeckillUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        //延长有效期
        SeckillUser user= redisService.get(SeckillUserKey.token,token,SeckillUser.class);
        if (user!=null){

            addCookie(response,token,user);
        }

        return user;
    }

    private void addCookie(HttpServletResponse response,String token,SeckillUser user){


        redisService.set(SeckillUserKey.token,token,user);
        Cookie cookie=new Cookie(COOK_NAME_TOKEN,token);
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);

    }
}
