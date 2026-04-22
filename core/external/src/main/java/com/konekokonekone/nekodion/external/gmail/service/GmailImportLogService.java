package com.konekokonekone.nekodion.external.gmail.service;

import com.konekokonekone.nekodion.external.gmail.mapper.GmailImportLogMapper;
import com.konekokonekone.nekodion.external.gmail.repository.GmailImportLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GmailImportLogService {

    private final GmailImportLogRepository repository;

    private final GmailImportLogMapper mapper;

    public boolean isAlreadyImported(String userId, String gmailMessageId) {
        return repository.existsByUserIdAndGmailMessageId(userId, gmailMessageId);
    }

    public void record(String userId, Long accountId, String gmailMessageId,
                       LocalDateTime transactionDateTime, BigDecimal amount, String shopName) {
        var log = mapper.toEntity(userId, accountId, gmailMessageId, transactionDateTime, amount, shopName);
        repository.save(log);
    }
}
