package com.konekokonekone.nekodion.external.gmail.crypto;

import com.konekokonekone.nekodion.support.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenEncryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private static final int IV_LENGTH_BYTES = 12;

    private static final int TAG_LENGTH_BITS = 128;

    private final SecretKey key;

    public TokenEncryptor(@Value("${gmail.encryption-key}") String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("gmail.encryption-key must be 32 bytes (256-bit), base64-encoded");
        }
        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) {
        if (plaintext == null) return null;
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
            byte[] result = new byte[IV_LENGTH_BYTES + ciphertext.length];
            System.arraycopy(iv, 0, result, 0, IV_LENGTH_BYTES);
            System.arraycopy(ciphertext, 0, result, IV_LENGTH_BYTES, ciphertext.length);
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            throw new ExternalApiException("トークンの暗号化に失敗しました。", e);
        }
    }

    public String decrypt(String encrypted) {
        if (encrypted == null) return null;
        try {
            byte[] data = Base64.getDecoder().decode(encrypted);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            System.arraycopy(data, 0, iv, 0, IV_LENGTH_BYTES);
            byte[] ciphertext = new byte[data.length - IV_LENGTH_BYTES];
            System.arraycopy(data, IV_LENGTH_BYTES, ciphertext, 0, ciphertext.length);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(ciphertext));
        } catch (Exception e) {
            throw new ExternalApiException("トークンの復号化に失敗しました。", e);
        }
    }
}
