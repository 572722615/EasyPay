package com.erban.common.secretkey;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;


    public static byte[] getAESkey() {
        byte[] randomKey = new byte[16];
        new SecureRandom().nextBytes(randomKey);

        return randomKey;
    }

    public AES(byte[] keyBytes) {
        try {
            byte[] buf = new byte[16];

            for (int i = 0; i < keyBytes.length && i < buf.length; i++) {
                buf[i] = keyBytes[i];
            }

            this.keySpec = new SecretKeySpec(buf, "AES");
            this.ivSpec = new IvParameterSpec(keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] origData) {
        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
            cipher.init(Cipher.ENCRYPT_MODE, this.keySpec);

            return cipher.doFinal(origData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] crypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.keySpec);
            return cipher.doFinal(crypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//	public static String base64Encode(byte[] data) throws UnsupportedEncodingException {
//		return new String (Base64.encode(data, Base64.DEFAULT), "utf-8");
//	}

}