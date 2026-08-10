package com.konekokonekone.nekodion.ingest.usecase;

import com.konekokonekone.nekodion.emailingest.service.EmailImportLogService;
import com.konekokonekone.nekodion.ingest.request.IngestEmailRequest;
import com.konekokonekone.nekodion.ingest.service.*;
import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.service.AccountService;
import com.konekokonekone.nekodion.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.konekokonekone.nekodion.transaction.constant.AccountTemplate.*;

@Service
@RequiredArgsConstructor
@Transactional
public class IngestUseCase {

    private final UserService userService;

    private final AccountService accountService;

    private final EmailImportLogService emailImportLogService;

    private final SmbcBankDepositImportService smbcBankDepositImportService;

    private final SmbcBankWithdrawalImportService smbcBankWithdrawalImportService;

    private final SmbcBankDirectDebitImportService smbcBankDirectDebitImportService;

    private final SmbcCardImportService smbcCardImportService;

    private final JcbCardImportService jcbCardImportService;

    public void process(IngestEmailRequest request) {
        var user = userService.findByEmailForwardToken(request.getToken());

        if (request.getMessageId() != null
                && emailImportLogService.isAlreadyImported(user.getId(), request.getMessageId())) {
            return;
        }

        var accounts = accountService.findByUserIdWithTemplate(user.getId());

        if (jcbCardImportService.matches(request)) {
            jcbCardImportService.execute(user.getId(), resolveAccount(accounts, JCB_CARD_TEMPLATE_ID), request);
        } else if (smbcCardImportService.matches(request)) {
            smbcCardImportService.execute(user.getId(), resolveAccount(accounts, SMBC_CARD_TEMPLATE_ID), request);
        } else if (smbcBankDepositImportService.matches(request)) {
            smbcBankDepositImportService.execute(user.getId(), resolveAccount(accounts, SMBC_BANK_TEMPLATE_ID), request);
        } else if (smbcBankWithdrawalImportService.matches(request)) {
            smbcBankWithdrawalImportService.execute(user.getId(), resolveAccount(accounts, SMBC_BANK_TEMPLATE_ID), request);
        } else if (smbcBankDirectDebitImportService.matches(request)) {
            smbcBankDirectDebitImportService.execute(user.getId(), resolveAccount(accounts, SMBC_BANK_TEMPLATE_ID), request);
        } else {
            throw new IllegalArgumentException("対応していないメール形式です");
        }

        if (request.getMessageId() != null) {
            var receivedAt = LocalDateTime.ofInstant(request.getReceivedAt(), ZoneId.systemDefault());
            emailImportLogService.record(user.getId(), request.getMessageId(), receivedAt);
        }
    }

    private Account resolveAccount(List<Account> accounts, long templateId) {
        var matched = accounts.stream()
                .filter(account -> account.getAccountTemplate() != null
                        && account.getAccountTemplate().getId() == templateId)
                .toList();

        if (matched.isEmpty()) {
            throw new EntityNotFoundException(String.format("メールに対応する口座が登録されていません。templateId[%d]", templateId));
        }
        if (matched.size() > 1) {
            throw new EntityNotFoundException(String.format("メールに対応する口座を一意に特定できません。templateId[%d]", templateId));
        }
        return matched.getFirst();
    }
}
