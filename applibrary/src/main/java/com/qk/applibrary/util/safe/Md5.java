package com.qk.applibrary.util.safe;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者：zhoubenhua
 * 时间：2016-10-20 12:59
 * 功能:md5加密
 */
public class Md5 {
    /**
     * 加密
     * @param str 要加密的字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException
     */
    public static String encrypt(String str){
        String enctyptStr = "";
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] m = md5.digest();//加密
            enctyptStr = getString(m);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return enctyptStr;

    }
    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
