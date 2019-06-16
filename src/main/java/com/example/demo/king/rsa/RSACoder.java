package com.example.demo.king.rsa;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSACoder {
    public static void main(String[] args) throws Exception {
        /*KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(1024);
        KeyPair keyPair = rsa.generateKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();

        System.out.println("公钥："+ Base64.getEncoder().encodeToString(aPublic.getEncoded()));
        System.out.println("私钥："+Base64.getEncoder().encodeToString(aPrivate.getEncoded()));*/
        String gongYao = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCu+uzfU71hhadBzs+aCh182sdEilly3xBiuGXAabRNDaODhRLDsyqKHDBih6JLFnV5QUmxgyOjrbVrR1bey6hyJeHy6Y+Uv8x2QyYN6vJ2xLOLufGf1XA1Nl6WSFYrZ8kmIsjv6pwXZKCq4cqwjN+qwiTL0fGHDR3EKUeJVvgT6wIDAQAB";
        String siYao = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK767N9TvWGFp0HOz5oKHXzax0SKWXLfEGK4ZcBptE0No4OFEsOzKoocMGKHoksWdXlBSbGDI6OttWtHVt7LqHIl4fLpj5S/zHZDJg3q8nbEs4u58Z/VcDU2XpZIVitnySYiyO/qnBdkoKrhyrCM36rCJMvR8YcNHcQpR4lW+BPrAgMBAAECgYEAnOTbPYftWCMqDISuetU1CX11jQxsFebS32GtwrBE/Y4MHmJUp+rfasTJ0Z8CM0eo/5c9x4/phdq8v7tQtiZwGqR7IcXyaOsR0VWFKWtkQEUaj9/oiA5TMFDXA+cp0QbRUvF9u9dzJo79MRI8D4cNd8niLHVLHnnfOSwEDWmrzVkCQQDo2kNRKydXC1fm+ib7VoBTmtuG/uTz/Ze0KX2DzYL8aS7LSWHyKGJ2Gp/kWNdrg1HfKGWpwWTC9rs00fgIBs5XAkEAwF/mNBVe8Y7Li9Msgs2bM9GiVshnDuXYhpjoxGeDUABmKJAblf27sUAx9zJrNzwSGvikbBKUoCaYQ8W71mpCjQJASv44ngSq9+LEXbSFJ391dvex6ilfraEn4bNXJELlA8wQcRJiRC5zf5wVkOJ4Br1Veey1yVVq0UvLgTh6jIM8owJBAL0MgRml9svD3FvohtF0ZJUj6jkGb+DAF20OT5DTeMPh4IL0O2fHFfA2eePHdH2vVsuSlWJvUAeiZfgUFWe8vPECQGwSMP8SSo2evALuz3K/yRtXZ3Abe490CV2FUn5YC7ud4fSjhrtgGAjauICN7VCGXtJypGzyKZG8pnOZYLwDiow=";


        PublicKey publicKey = getPublicKey(gongYao);
        PrivateKey privateKey = getPrivateKey(siYao);
        String s = "毛毛球k2";
        byte[] encryptByte = encrypt(s, privateKey);
        String encrypt = Base64.getEncoder().encodeToString(encryptByte);
        System.out.println("私钥加密："+encrypt);
        byte[] decryptByte = decrypt(Base64.getDecoder().decode(encrypt), publicKey);
        System.out.println("公钥解密:"+new String(decryptByte,StandardCharsets.UTF_8));
    }

    /**
     * 通过字符串生成私钥
     */
    public static PrivateKey getPrivateKey(String privateKeyData) {
        PrivateKey privateKey = null;
        try {
            byte[] decodeKey = Base64.getDecoder().decode(privateKeyData); //将字符串Base64解码
            PKCS8EncodedKeySpec x509 = new PKCS8EncodedKeySpec(decodeKey);//创建x509证书封装类
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");//指定RSA
            privateKey = keyFactory.generatePrivate(x509);//生成私钥
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    /**
     * 通过字符串生成公钥
     */
    public static PublicKey getPublicKey(String publicKeyData) {
        PublicKey publicKey = null;
        try {
            byte[] decodeKey = Base64.getDecoder().decode(publicKeyData);
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(x509);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 加密
     */
    public static byte[] encrypt(String data, Key key) {
        try {
            //取公钥
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
