package com.konekokonekone.nekodion.external.gmail.service;

import com.konekokonekone.nekodion.support.exception.ExternalApiException;
import com.konekokonekone.nekodion.support.exception.InvalidOAuthStateException;
import com.konekokonekone.nekodion.support.exception.OAuthStateExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class GmailOAuthStateService {

    private static final int STATE_EXPIRY_MINUTES = 10;

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] secretKey;

    public GmailOAuthStateService(@Value("${gmail.state-secret}") String base64Secret) {
        this.secretKey = Base64.getDecoder().decode(base64Secret);
    }

    /**
     * ユーザーIDをエンコードして、HMAC署名を付与した状態トークンを生成する。
     *
     * @param userId ユーザーID
     * @return 状態トークン（例: base64(userId):expiryMs.signature）
     */
    public String createState(String userId) {
        String encodedUserId = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(userId.getBytes(StandardCharsets.UTF_8));
        long expiryMs = System.currentTimeMillis() + STATE_EXPIRY_MINUTES * 60 * 1000L;
        String payload = encodedUserId + ":" + expiryMs;
        return payload + "." + sign(payload);
    }

    /**
     * 状態トークンを検証して、ユーザーIDをデコードして返す。
     *
     * @param state 状態トークン
     * @return ユーザーID
     */
    public String consumeState(String state) {
        int dotIndex = state.lastIndexOf('.');
        if (dotIndex < 0) throw new InvalidOAuthStateException("OAuth stateが不正です。");

        String payload = state.substring(0, dotIndex);
        String sig = state.substring(dotIndex + 1);
        if (!MessageDigest.isEqual(sign(payload).getBytes(StandardCharsets.UTF_8), sig.getBytes(StandardCharsets.UTF_8))) {
            throw new InvalidOAuthStateException("OAuth stateが不正です。");
        }

        int colonIndex = payload.lastIndexOf(':');
        long expiryMs = Long.parseLong(payload.substring(colonIndex + 1));
        if (System.currentTimeMillis() > expiryMs) {
            throw new OAuthStateExpiredException("OAuth stateの有効期限が切れています。");
        }

        String encodedUserId = payload.substring(0, colonIndex);
        return new String(Base64.getUrlDecoder().decode(encodedUserId), StandardCharsets.UTF_8);
    }

    /**
     * HMAC署名を生成する
     *
     * @param data 署名対象のデータ
     * @return HMAC署名のBase64エンコード文字列
     */
    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secretKey, HMAC_ALGORITHM));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new ExternalApiException("HMAC署名に失敗しました。", e);
        }
    }
}
