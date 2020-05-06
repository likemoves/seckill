package com.wzh.seckill.exception;

import com.wzh.seckill.result.CodeMsg;

/**
 * Created by admin on 2020/5/5.
 */
public class GlobleException extends RuntimeException{

    private static final long serivalVersionUID=1L;

    private CodeMsg cm;

    public GlobleException(CodeMsg cm){
        super(cm.toString());
        this.cm=cm;
    }

    public CodeMsg getCm(){
        return cm;
    }
}
