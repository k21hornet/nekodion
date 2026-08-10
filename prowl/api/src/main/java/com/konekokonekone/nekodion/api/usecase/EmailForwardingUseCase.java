package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.response.EmailForwardingAddressResponse;
import com.konekokonekone.nekodion.api.response.EmailForwardingConfirmationResponse;
import com.konekokonekone.nekodion.emailingest.service.EmailForwardingConfirmationService;
import com.konekokonekone.nekodion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailForwardingUseCase {

    private final UserService userService;

    private final EmailForwardingConfirmationService emailForwardingConfirmationService;

    @Value("${email-forwarding.mail-domain}")
    private String mailDomain;

    /**
     * ユーザーのカード明細メール転送先アドレスを取得する。未発行の場合はtokenを新規発行する
     *
     * @param userId ユーザーID
     * @return メール転送先アドレスを含むレスポンス
     */
    public EmailForwardingAddressResponse getAddress(String userId) {
        var token = userService.getOrCreateEmailForwardToken(userId);
        return EmailForwardingAddressResponse.builder()
                .address("card-" + token + "@" + mailDomain)
                .build();
    }

    /**
     * ユーザーの最新のメール転送確認コードを取得する
     *
     * @param userId ユーザーID
     * @return メール転送確認コードを含むレスポンス
     */
    public Optional<EmailForwardingConfirmationResponse> getConfirmation(String userId) {
        return emailForwardingConfirmationService.findByUserId(userId)
                .map(dto -> EmailForwardingConfirmationResponse.builder()
                        .fromAddress(dto.getFromAddress())
                        .subject(dto.getSubject())
                        .bodyText(dto.getBodyText())
                        .receivedAt(dto.getReceivedAt())
                        .build());
    }
}
