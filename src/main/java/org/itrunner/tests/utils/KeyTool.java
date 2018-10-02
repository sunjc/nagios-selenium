package org.itrunner.tests.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

public class KeyTool {
    private static final String KEY_STRING = "b2qZDg+KRU54pyYpzm0kwQ==";
    private static final String CIPHER = "AES";

    private KeyTool() {

    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encrypt("123@welcome"));
    }

    private static String encrypt(String password) throws Exception {
        Cipher cipher = getCipher();
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        byte[] cipherText = cipher.doFinal(password.getBytes());
        return encodeBase64String(cipherText);
    }

    public static String decrypt(String cipherText) {
        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] password = cipher.doFinal(decodeBase64(cipherText));
            return new String(password);
        } catch (Exception e) {
            return null;
        }
    }

    private static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CIPHER, PROVIDER_NAME);
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        return encodeBase64String(key.getEncoded());
    }

    private static Key getKey() {
        return new SecretKeySpec(decodeBase64(KEY_STRING), CIPHER);
    }

    private static Cipher getCipher() throws Exception {
        return Cipher.getInstance("AES/ECB/PKCS5Padding", PROVIDER_NAME);
    }
}