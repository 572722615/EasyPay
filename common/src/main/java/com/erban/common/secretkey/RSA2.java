package com.erban.common.secretkey;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class RSA2 {

    public static final String PUBLIC_KEY = "MDcwDQYJKoZIhvcNAQEBBQADJgAwIwIcAN4ev8NwvaxP22fQv/fUGc8/uNnfjHde" + "CgqUPQIDAQAB";

    //    KeyPairGenerator            kpg;
//    KeyPair                     kp;
    RSAPublicKey publicKey;
    //    PrivateKey                  privateKey  ;
//    byte [] encryptedBytes, decryptedBytes;
    Cipher cipher1;
//    String                      decrypted;


    public RSA2() {
        initPubkey();
    }

    private void initPubkey() {

        String sPubExponent = "65537";

        try {
            publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(PUBLIC_KEY), new BigInteger(sPubExponent)));

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public byte[] rsaDecrypt(final byte[] enCryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        if (publicKey == null)
            return null;

        cipher1 = Cipher.getInstance("RSA");

        //use public key decrypt.
        cipher1.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedBytes = cipher1.doFinal(enCryptedBytes);
//        decrypted = new String(decryptedBytes);
        return decryptedBytes;
    }


}


