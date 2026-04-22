package com.konekokonekone.nekodion.external.gmail.repository;

import com.konekokonekone.nekodion.external.gmail.entity.GmailImportLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GmailImportLogRepository extends JpaRepository<GmailImportLog, Long> {

    boolean existsByUserIdAndGmailMessageId(String userId, String gmailMessageId);
}
