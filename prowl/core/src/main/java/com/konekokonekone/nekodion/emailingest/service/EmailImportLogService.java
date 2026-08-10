package com.konekokonekone.nekodion.emailingest.service;

import com.konekokonekone.nekodion.emailingest.entity.EmailImportLog;
import com.konekokonekone.nekodion.emailingest.repository.EmailImportLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailImportLogService {

    private final EmailImportLogRepository emailImportLogRepository;

    /**
     * 同一ユーザー・同一メールが取込み済みかどうかを判定する
     *
     * @param userId    ユーザーID
     * @param messageId メールのMessage-ID
     * @return 取込み済みの場合true
     */
    public boolean isAlreadyImported(String userId, String messageId) {
        return emailImportLogRepository.existsByUserIdAndMessageId(userId, messageId);
    }

    /**
     * メール取込み済みログを記録する
     *
     * @param userId     ユーザーID
     * @param messageId  メールのMessage-ID
     * @param receivedAt メール受信日時
     */
    public void record(String userId, String messageId, LocalDateTime receivedAt) {
        var log = new EmailImportLog();
        log.setUserId(userId);
        log.setMessageId(messageId);
        log.setReceivedAt(receivedAt);
        emailImportLogRepository.save(log);
    }
}
