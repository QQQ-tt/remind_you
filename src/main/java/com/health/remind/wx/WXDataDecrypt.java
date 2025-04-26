package com.health.remind.wx;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.util.Base64;

/**
 * @author qtx
 * @since 2025/4/26 21:54
 */
public class WXDataDecrypt {

    /**
     * 解密微信小程序 encryptedData
     * @param encryptedData 加密的数据
     * @param sessionKey 登录时获取的session_key
     * @param iv 初始化向量
     * @return 解密后的明文JSON
     * @throws Exception 抛出异常自己处理
     */
    public static String decrypt(String encryptedData, String sessionKey, String iv) throws Exception {
        // 1. Base64 decode
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        byte[] keyByte = Base64.getDecoder().decode(sessionKey);
        byte[] ivByte = Base64.getDecoder().decode(iv);

        // 2. 如果keyByte长度不足16位，需要补齐
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + 1;
            byte[] temp = new byte[groups * base];
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }

        // 3. 初始化 Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);

        // 4. 执行解密
        byte[] resultByte = cipher.doFinal(dataByte);
        if (resultByte != null && resultByte.length > 0) {
            return new String(resultByte, "UTF-8");
        }
        return null;
    }
}
