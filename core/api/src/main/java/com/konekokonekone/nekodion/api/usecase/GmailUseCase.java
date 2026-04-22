package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.response.GmailMessageResponse;
import com.konekokonekone.nekodion.external.gmail.service.GmailClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GmailUseCase {

    private final GmailClientService gmailClientService;

    /**
     * ユーザーのGmailから指定されたクエリに基づいてメールを取得する
     *
     * @param userId ユーザーID
     * @param query Gmailの検索クエリ（例: "is:unread", "from: 三井住友銀行", "subject:明細" など）
     * @param maxResults 取得する最大件数
     * @return GmailMessageレスポンスリスト
     */
    public List<GmailMessageResponse> getMessages(String userId, String query, int maxResults) {
        return gmailClientService.fetchMessages(userId, query, maxResults).stream()
                .map(msg -> GmailMessageResponse.builder()
                        .id(msg.getId())
                        .subject(msg.getSubject())
                        .from(msg.getFrom())
                        .date(msg.getDate())
                        .body(msg.getBody())
                        .build())
                .toList();
    }
}
