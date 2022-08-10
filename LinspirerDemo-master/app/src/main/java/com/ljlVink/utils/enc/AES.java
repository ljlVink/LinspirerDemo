package com.ljlVink.utils.enc;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static final String key="1191ADF18489D8DA";
    public static final String VIPARA = "5E9B755A8B674394";
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    public static final String CODE_TYPE = "UTF-8";
    public static String encrypt(String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(cleartext.getBytes(CODE_TYPE));
            return new String(Base64.encode(result, Base64.NO_WRAP),CODE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] encrypt(byte[] clear) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(CODE_TYPE));
        byte[] raw = key.getBytes(CODE_TYPE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, zeroIv);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }
    public static String decrypt(String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode(encrypted.getBytes(CODE_TYPE), Base64.NO_WRAP);
            byte[] result = decrypt(enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] decrypt(byte[] encrypted) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(CODE_TYPE));
        byte[] raw = key.getBytes(CODE_TYPE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, zeroIv);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}