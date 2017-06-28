package com.qk.applibrary.util.safe;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 作者：zhoubenhua
 * 时间：2016-10-26 13:23
 * 功能:rsa加密和解密算法
 */
public class RSA {
    private static final String TAG = "RSA";
    private static String RSA_ANDROID = "RSA/ECB/PKCS1Padding";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws  Exception {
        byte[] decodedKey = Base64.decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 加密
     * @param content 要加密字符串
     * @param publicKey 公钥
     * @return 加密之后字符串
     */
//    public static String encrypt(String content, String publicKey) {
//        try {
//            PublicKey pubkey = getPublicKeyFromX509(TAG, publicKey);
//
//            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
//            byte plaintext[] = content.getBytes("UTF-8");
//            byte[] output = cipher.doFinal(plaintext);
//            String s = new String(Base64.encode(output));
//            return s;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    public static String encrypt(String content, String publickey) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(TAG, publickey);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte output[] = content.getBytes("UTF-8");
//			byte[] output = cipher.doFinal(plaintext);
//			String s = new String(Base64.encode(output));

            int inputLen = output.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(output, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(output, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();


            String s = new String(Base64.encode(encryptedData));


            return s;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用私钥解密
     * @param content：加密的字符串
     * @param privateKey：私钥
     * @return：解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String content, String privateKey) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(RSA_ANDROID);
        cipher.init(Cipher.DECRYPT_MODE, prikey);
        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;
            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }
            writer.write(cipher.doFinal(block));
        }
        return new String(writer.toByteArray(),"utf-8");
    }

    /**
     * 得到私钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;

        keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(TAG);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

}
