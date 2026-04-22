package com.konekokonekone.nekodion.external.gmail.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.konekokonekone.nekodion.external.gmail.dto.GmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GmailClientService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final GmailOAuthService oAuthService;

    /**
     * Gmailのメッセージを検索して取得する
     *
     * @param userId ユーザーID
     * @param query Gmailの検索クエリ（例: "is:unread", "from:三井住友銀行", "subject:明細" など）
     * @param maxResults 取得する最大件数
     * @return GmailMessages
     */
    public List<GmailMessage> fetchMessages(String userId, String query, int maxResults) {
        try {
            Credential credential = oAuthService.loadCredential(userId);
            if (credential == null) {
                throw new IllegalStateException("User not authorized. userId=" + userId);
            }
            Gmail gmail = new Gmail.Builder(oAuthService.getHttpTransport(), JSON_FACTORY, credential)
                    .setApplicationName("nekodion")
                    .build();

            var response = gmail.users().messages().list("me")
                    .setQ(query)
                    .setMaxResults((long) maxResults)
                    .execute();

            List<GmailMessage> result = new ArrayList<>();
            if (response.getMessages() == null) return result;

            for (Message msg : response.getMessages()) {
                Message detail = gmail.users().messages().get("me", msg.getId())
                        .setFormat("full")
                        .execute();
                result.add(toGmailMessage(detail));
            }
            return result;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Gmailの取得に失敗しました。 ユーザーID[%s]", userId), e);
        }
    }

    private GmailMessage toGmailMessage(Message message) {
        String subject = "", from = "", date = "";
        if (message.getPayload() != null && message.getPayload().getHeaders() != null) {
            for (MessagePartHeader header : message.getPayload().getHeaders()) {
                switch (header.getName()) {
                    case "Subject" -> subject = header.getValue();
                    case "From" -> from = header.getValue();
                    case "Date" -> date = header.getValue();
                }
            }
        }
        String body = extractBody(message.getPayload());
        return new GmailMessage(message.getId(), subject, from, date, body);
    }

    private String extractBody(MessagePart part) {
        if (part == null) return "";

        // text/plain を優先、なければ text/html
        if ("text/plain".equals(part.getMimeType()) || "text/html".equals(part.getMimeType())) {
            if (part.getBody() != null && part.getBody().getData() != null) {
                byte[] decoded = Base64.getUrlDecoder().decode(part.getBody().getData());
                return new String(decoded, StandardCharsets.UTF_8);
            }
        }

        if (part.getParts() != null) {
            String plainBody = null;
            String htmlBody = null;
            for (MessagePart child : part.getParts()) {
                String childBody = extractBody(child);
                if (!childBody.isEmpty()) {
                    if ("text/plain".equals(child.getMimeType())) {
                        plainBody = childBody;
                    } else if (plainBody == null && "text/html".equals(child.getMimeType())) {
                        htmlBody = childBody;
                    } else if (plainBody == null && htmlBody == null) {
                        htmlBody = childBody;
                    }
                }
            }
            if (plainBody != null) return plainBody;
            if (htmlBody != null) return htmlBody;
        }

        return "";
    }
}