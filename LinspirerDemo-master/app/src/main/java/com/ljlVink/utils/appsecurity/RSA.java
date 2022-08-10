package com.ljlVink.utils.appsecurity;

import com.ljlVink.utils.DataUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class RSA {
    public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String KEY_ALGORITHM = "RSA";
    private static final int MAX_DECRYPT_BLOCK = 65535;
    public static PublicKey getPublicKey(String pubKey) {
        try {
            byte[] publicKey = DataUtils.base64Decode(pubKey);
            return KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(
                    new X509EncodedKeySpec(publicKey));
        } catch (Exception e) {
        }
        return null;
    }
    public static String decryptByPublicKey(String encryptBase64Data, String pubKey) {
        try {
            byte[] encryptedData = DataUtils.base64Decode(encryptBase64Data);
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, getPublicKey(pubKey));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0, i = 0;
            byte[] cache;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, CHARSET_UTF8);
        } catch (Exception e) {
        }
        return "null";
    }

}