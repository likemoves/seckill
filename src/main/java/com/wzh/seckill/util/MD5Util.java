package com.wzh.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by admin on 2020/5/5.
 */
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    //默认盐
    private static final String salt="1a2b3c4d";

    /**
     * 对明文密码第一次进行md5加密
     * @param inputPass  明文密码
     * @return
     */
    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 对已经加过一次密的密码，进行第二次MD5加密
     * @param formPass  加密过一次的密码
     * @param salt    随机生成的盐
     * @return
     */
    public static String formPassToDBPass(String formPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPassToFormPass(formPass)+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 对密码进行校验
     * @param input  明文密码
     * @param saltDB 第二次MD5加密 盐
     * @return
     */
    public static String inputPassToDbPass(String input,String saltDB){
        String formPass=inputPassToFormPass(input);
        String dbPass=formPassToDBPass(formPass,saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"),"1a2b3c4d"));//a69eead8138f909fe5b79e9f95971a82
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));


    }

}
