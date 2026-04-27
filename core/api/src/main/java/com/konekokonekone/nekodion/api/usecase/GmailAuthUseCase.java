package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.response.GmailAuthUrlResponse;
import com.konekokonekone.nekodion.api.response.GmailStatusResponse;
import com.konekokonekone.nekodion.support.exception.ExternalApiException;
import com.konekokonekone.nekodion.external.gmail.service.GmailOAuthService;
import com.konekokonekone.nekodion.external.gmail.service.GmailOAuthStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GmailAuthUseCase {

    private final GmailOAuthService oAuthService;
    private final GmailOAuthStateService oAuthStateService;

    @Value("${gmail.redirect-uri}")
    private String redirectUri;

    /**
     * ユーザーのGmail認証URLを生成して返す
     *
     * @param userId ユーザーID
     * @return GmailAuthUrlResponse認証URLを含むレスポンス
     */
    public GmailAuthUrlResponse getAuthUrl(String userId) {
        String state = oAuthStateService.createState(userId);
        String url = oAuthService.buildAuthorizationUrl(state, redirectUri);
        return GmailAuthUrlResponse.builder().url(url).build();
    }

    /**
     * Googleからのコールバックを処理し、認証コードをアクセストークンに交換する
     *
     * @param code Googleから返された認証コード
     * @param state state
     */
    public void handleCallback(String code, String state) {
        String userId = oAuthStateService.consumeState(state);
        try {
            oAuthService.exchangeCodeForToken(userId, code, redirectUri);
        } catch (Exception e) {
            throw new ExternalApiException("Gmail OAuthコードの交換に失敗しました。", e);
        }
    }

    /**
     * ユーザーのGmail認証状態を確認する
     *
     * @param userId ユーザーID
     * @return 認証状態を含むレスポンス
     */
    public GmailStatusResponse getStatus(String userId) {
        try {
            return GmailStatusResponse.builder()
                    .connected(oAuthService.isAuthorized(userId))
                    .build();
        } catch (Exception e) {
            throw new ExternalApiException("Gmailの認証状態確認に失敗しました。", e);
        }
    }
}
