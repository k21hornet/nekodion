package com.konekokonekone.nekodion.emailingest.service;

import com.konekokonekone.nekodion.emailingest.dto.EmailForwardingConfirmationDto;
import com.konekokonekone.nekodion.emailingest.entity.EmailForwardingConfirmation;
import com.konekokonekone.nekodion.emailingest.mapper.EmailForwardingConfirmationMapper;
import com.konekokonekone.nekodion.emailingest.repository.EmailForwardingConfirmationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailForwardingConfirmationService {

    private final EmailForwardingConfirmationRepository emailForwardingConfirmationRepository;

    private final EmailForwardingConfirmationMapper emailForwardingConfirmationMapper;

    /**
     * メール転送確認コードを保存する。ユーザーごとに最新の1件のみ保持する
     *
     * @param userId      ユーザーID
     * @param fromAddress 確認メールの差出人
     * @param subject     確認メールの件名
     * @param bodyText    確認メール本文
     * @param receivedAt  確認メール受信日時
     */
    public void saveOrUpdate(String userId, String fromAddress, String subject, String bodyText, LocalDateTime receivedAt) {
        var confirmation = emailForwardingConfirmationRepository.findById(userId)
                .orElseGet(EmailForwardingConfirmation::new);

        confirmation.setUserId(userId);
        confirmation.setFromAddress(fromAddress);
        confirmation.setSubject(subject);
        confirmation.setBodyText(bodyText);
        confirmation.setReceivedAt(receivedAt);

        emailForwardingConfirmationRepository.save(confirmation);
    }

    /**
     * ユーザーの最新のメール転送確認コードを取得する
     *
     * @param userId ユーザーID
     * @return メール転送確認コード
     */
    public Optional<EmailForwardingConfirmationDto> findByUserId(String userId) {
        return emailForwardingConfirmationRepository.findById(userId)
                .map(emailForwardingConfirmationMapper::toDto);
    }
}
