package com.taobao.tae.sdk.demo.model;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class TokenInfoBuilder {
    private static final IvParameterSpec IVSPEC = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    private static final String BASE64_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    private static final char[] BASE64 = BASE64_STRING
            .toCharArray();
    private static final int MASK6 = 0x3f,
            MASK8 = 0xff;

    public static String buildToken(Long openAccountId, String appSecret) {
        try {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.openAccountId = openAccountId;
            tokenInfo.timestamp = System.currentTimeMillis();
            tokenInfo.loginStateExpireIn = 30 * 24 * 3600 * 1000;
            return bytes2base64(aesEncrypt(tokenInfo.toJsonString().getBytes("UTF-8"), getMD5(appSecret)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * to base64 string.
     *
     * @param b byte array.
     * @return base64 string.
     */
    public static String bytes2base64(byte[] b) {
        return bytes2base64(b, 0, b.length);
    }

    /**
     * to base64 string.
     *
     * @param b byte array.
     * @return base64 string.
     */
    public static String bytes2base64(byte[] b, int offset, int length) {
        return bytes2base64(b, offset, length, BASE64, true);
    }

    private static String bytes2base64(byte[] bs, int off, int len, char[] code, boolean pad) {
        if (off < 0)
            throw new IndexOutOfBoundsException("bytes2base64: offset < 0, offset is " + off);
        if (len < 0)
            throw new IndexOutOfBoundsException("bytes2base64: length < 0, length is " + len);
        if (off + len > bs.length)
            throw new IndexOutOfBoundsException("bytes2base64: offset + length > array length.");

        int num = len / 3, rem = len % 3, r = off, w = 0;
        char[] cs = new char[num * 4 + (rem == 0 ? 0 : pad ? 4 : rem + 1)];

        for (int i = 0; i < num; i++) {
            int b1 = bs[r++] & MASK8, b2 = bs[r++] & MASK8, b3 = bs[r++] & MASK8;

            cs[w++] = code[b1 >> 2];
            cs[w++] = code[(b1 << 4) & MASK6 | (b2 >> 4)];
            cs[w++] = code[(b2 << 2) & MASK6 | (b3 >> 6)];
            cs[w++] = code[b3 & MASK6];
        }

        if (rem == 1) {
            int b1 = bs[r++] & MASK8;
            cs[w++] = code[b1 >> 2];
            cs[w++] = code[(b1 << 4) & MASK6];
            if (pad) {
                cs[w++] = code[64];
                cs[w++] = code[64];
            }
        } else if (rem == 2) {
            int b1 = bs[r++] & MASK8, b2 = bs[r++] & MASK8;
            cs[w++] = code[b1 >> 2];
            cs[w++] = code[(b1 << 4) & MASK6 | (b2 >> 4)];
            cs[w++] = code[(b2 << 2) & MASK6];
            if (pad)
                cs[w++] = code[64];
        }
        return new String(cs);
    }

    private static byte[] getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return m;
    }

    private static byte[] aesEncrypt(byte[] content, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IVSPEC);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}
