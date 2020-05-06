package com.wzh.seckill.exception;

import com.wzh.seckill.controller.GoodsController;
import com.wzh.seckill.result.CodeMsg;
import com.wzh.seckill.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.validation.BindException;


import javax.servlet.http.HttpServletRequest;

import java.util.List;


/**
 * Created by admin on 2020/5/5.
 */
@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobleException.class);

    @ExceptionHandler(value=Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        if(e instanceof GlobleException) {
            GlobleException ex= (GlobleException) e;
            log.info(e.getMessage());
            return Result.error(ex.getCm());
        }

        else if(e instanceof BindException){
            log.info(e.getMessage());
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            log.info(e.getMessage());
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

}
