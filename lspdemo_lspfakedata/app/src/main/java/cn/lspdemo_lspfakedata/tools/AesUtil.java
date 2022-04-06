package cn.lspdemo_lspfakedata.tools;

import android.text.TextUtils;
import android.util.Base64;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    //偏移量
    public static final String VIPARA = "5E9B755A8B674394";   //AES 为16位. DES 为8bytes
    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    //AES 加密
    private static final String AES = "AES";
    // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private static final String SHA1PRNG = "SHA1PRNG";
    //编码方式
    public static final String CODE_TYPE = "UTF-8";

    /**
     * 加密
     *
     * @param key
     * @param cleartext
     * @return
     */
    public static String encrypt(String key, String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(key, cleartext.getBytes(CODE_TYPE));
            return new String(Base64.encode(result, Base64.NO_WRAP),CODE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(String key, byte[] clear) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(CODE_TYPE));
        byte[] raw = key.getBytes(CODE_TYPE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, zeroIv);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * 解密
     *
     * @param key
     * @param encrypted
     * @return
     */
    public static String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode(encrypted.getBytes(CODE_TYPE), Base64.NO_WRAP);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes(CODE_TYPE));
       /* byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);*/
        byte[] raw = key.getBytes(CODE_TYPE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, zeroIv);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }



}
