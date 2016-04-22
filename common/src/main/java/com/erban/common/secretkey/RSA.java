package com.erban.common.secretkey;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class RSA {

//    private static final byte[] DEFAULT_PUBLIC_KEY_ARRAY = {(byte) 0x30,
//            (byte) 0x81, (byte) 0x9f, (byte) 0x30, (byte) 0x0d, (byte) 0x06,
//            (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86,
//            (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x01,
//            (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81, (byte) 0x8d,
//            (byte) 0x00, (byte) 0x30, (byte) 0x81, (byte) 0x89, (byte) 0x02,
//            (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0xba, (byte) 0xdf,
//            (byte) 0x4a, (byte) 0x9e, (byte) 0xb1, (byte) 0xa2, (byte) 0x1f,
//            (byte) 0x73, (byte) 0x26, (byte) 0xa7, (byte) 0x6d, (byte) 0xeb,
//            (byte) 0x2c, (byte) 0x4e, (byte) 0xcb, (byte) 0x25, (byte) 0xc0,
//            (byte) 0x23, (byte) 0x96, (byte) 0xab, (byte) 0x85, (byte) 0x86,
//            (byte) 0x64, (byte) 0x27, (byte) 0xb3, (byte) 0xea, (byte) 0x4f,
//            (byte) 0x23, (byte) 0x3f, (byte) 0xc6, (byte) 0x51, (byte) 0x7d,
//            (byte) 0xa3, (byte) 0x42, (byte) 0xdc, (byte) 0xec, (byte) 0x0d,
//            (byte) 0x16, (byte) 0x90, (byte) 0x5b, (byte) 0x2b, (byte) 0xf0,
//            (byte) 0x1c, (byte) 0x6e, (byte) 0x86, (byte) 0xfb, (byte) 0x08,
//            (byte) 0xb1, (byte) 0x1a, (byte) 0x08, (byte) 0xa8, (byte) 0xe7,
//            (byte) 0xbc, (byte) 0xaa, (byte) 0xff, (byte) 0x56, (byte) 0x7c,
//            (byte) 0x39, (byte) 0x31, (byte) 0x0a, (byte) 0x9d, (byte) 0x14,
//            (byte) 0xb0, (byte) 0x5d, (byte) 0xbb, (byte) 0x17, (byte) 0x8b,
//            (byte) 0x09, (byte) 0xf1, (byte) 0x3c, (byte) 0x91, (byte) 0x21,
//            (byte) 0xef, (byte) 0x49, (byte) 0x71, (byte) 0x75, (byte) 0xf3,
//            (byte) 0x69, (byte) 0xaf, (byte) 0xf6, (byte) 0x04, (byte) 0x9d,
//            (byte) 0x75, (byte) 0xf3, (byte) 0x5e, (byte) 0x5b, (byte) 0x6b,
//            (byte) 0xfd, (byte) 0x8d, (byte) 0x41, (byte) 0x85, (byte) 0x98,
//            (byte) 0xb1, (byte) 0x21, (byte) 0xef, (byte) 0xca, (byte) 0x55,
//            (byte) 0x1b, (byte) 0x95, (byte) 0x27, (byte) 0xa7, (byte) 0x91,
//            (byte) 0xe0, (byte) 0x98, (byte) 0x04, (byte) 0x5e, (byte) 0xcc,
//            (byte) 0x87, (byte) 0x7c, (byte) 0x6f, (byte) 0x65, (byte) 0xd1,
//            (byte) 0x2b, (byte) 0x67, (byte) 0xc0, (byte) 0x7c, (byte) 0x9e,
//            (byte) 0x57, (byte) 0x54, (byte) 0xbd, (byte) 0x83, (byte) 0xdb,
//            (byte) 0xd1, (byte) 0x8e, (byte) 0xb2, (byte) 0xc2, (byte) 0xd3,
//            (byte) 0xd5, (byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x00,
//            (byte) 0x01
//
//    };

    private static final String DEFAULT_PUBLIC_KEY_ARRAY = "MDcwDQYJKoZIhvcNAQEBBQADJgAwIwIcAN4ev8NwvaxP22fQv/fUGc8/uNnfjHde" + "CgqUPQIDAQAB";
    /**
     * 公钥
     */
    private RSAPublicKey publicKey;


    /**
     * 获取公钥
     *
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(byte[] publicKeyArray) throws Exception {
        try {
            byte[] buffer = publicKeyArray;
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public void loadPublicKeyString(String publicKeyArray) throws Exception {
        try {
            byte[] result = Base64.decode(publicKeyArray, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(result);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public void init() {
        //long t = 0L;
        //加载公钥
        try {
            loadPublicKeyString(RSA.DEFAULT_PUBLIC_KEY_ARRAY);
//			System.out.println("加载公钥成功" + (t = System.currentTimeMillis()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
//			System.err.println("加载公钥失败");
        }
    }

    /**
     * 加密过程
     *
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }


}
