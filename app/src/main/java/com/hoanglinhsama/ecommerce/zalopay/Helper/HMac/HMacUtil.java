package com.hoanglinhsama.ecommerce.zalopay.Helper.HMac;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Tao HMAC (hien thi duoi dang chuoi Hex)
 */
public class HMacUtil {
    public final static String HMACSHA256 = "HmacSHA256"; // thuat toan tao ma hoa Hash-based Message Authentication Code dua tren thuat toan bam SHA-256

    /**
     * Tao ma hoa HMAC (cho data dau vao, va su dung koa bi mat) duoi dang mang byte
     */
    private static byte[] HMacEncode(final String algorithm, final String key, final String data) {
        Mac macGenerator = null;
        try {
            macGenerator = Mac.getInstance(algorithm);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            macGenerator.init(signingKey);
        } catch (Exception ex) {
        }
        if (macGenerator == null) {
            return null;
        }
        byte[] dataByte = null;
        try {
            dataByte = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return macGenerator.doFinal(dataByte);
    }

    /**
     * Tao ra MAC lien quan den ham bam mat ma ket hop voi khoa mat ma bi mat, ket qua tra ve duoc bieu dien duoi dang chuoi Hex
     *
     * @param algorithm Ham bam mat ma (MD5 hoac SHA-1)
     * @param key       khoa mat ma bi mat
     * @param data      message can xac thuc
     * @return Chuoi MAC duoi dang Hex
     */
    public static String HMacHexStringEncode(final String algorithm, final String key, final String data) {
        byte[] hmacEncodeBytes = HMacEncode(algorithm, key, data);
        if (hmacEncodeBytes == null) {
            return null;
        }
        return HexStringUtil.byteArrayToHexString(hmacEncodeBytes);
    }
}
