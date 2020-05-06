package com.wzh.seckill.config;

import com.wzh.seckill.domain.SeckillUser;
import com.wzh.seckill.service.SeckillUserService;
import groovy.util.IFileNameFinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2020/5/6.
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SeckillUserService seckillUserService;

    //判断方法中的参数是否符合要求
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz=methodParameter.getParameterType();
        return clazz== SeckillUser.class;
    }

    //符合要求  对参数进行整理
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request=nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response=nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken=request.getParameter(SeckillUserService.COOK_NAME_TOKEN);
        String cookieToken=getCookieValue(request,SeckillUserService.COOK_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            return null;
        }

        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return seckillUserService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request, String cookNameToken) {
        Cookie[] cookies=request.getCookies();
        for (Cookie c:cookies){
            if (c.getName().equals(cookNameToken)){
                return c.getValue();
            }
        }
        return null;
    }
}
