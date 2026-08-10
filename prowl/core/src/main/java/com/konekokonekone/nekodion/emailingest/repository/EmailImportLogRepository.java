package com.konekokonekone.nekodion.emailingest.repository;

import com.konekokonekone.nekodion.emailingest.entity.EmailImportLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailImportLogRepository extends JpaRepository<EmailImportLog, Long> {

    boolean existsByUserIdAndMessageId(String userId, String messageId);
}
