package com.konekokonekone.nekodion.batch.job;

import com.konekokonekone.nekodion.batch.runner.BatchResult;
import com.konekokonekone.nekodion.batch.runner.BatchResultStatus;
import com.konekokonekone.nekodion.batch.usecase.JcbCardImportUseCase;
import com.konekokonekone.nekodion.batch.usecase.SmbcBankDepositImportUseCase;
import com.konekokonekone.nekodion.batch.usecase.SmbcBankDirectDebitImportUseCase;
import com.konekokonekone.nekodion.batch.usecase.SmbcBankWithdrawalImportUseCase;
import com.konekokonekone.nekodion.batch.usecase.SmbcCardImportUseCase;
import com.konekokonekone.nekodion.external.gmail.entity.GmailCredentialEntity;
import com.konekokonekone.nekodion.external.gmail.repository.GmailCredentialRepository;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.repository.AccountRepository;
import com.konekokonekone.nekodion.user.entity.User;
import com.konekokonekone.nekodion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "batch.execute", havingValue = "gmail")
public class GmailImportJob implements BatchJob {

    private final GmailCredentialRepository gmailCredentialRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final SmbcBankDepositImportUseCase smbcBankDepositImportUseCase;

    private final SmbcBankWithdrawalImportUseCase smbcBankWithdrawalImportUseCase;

    private final SmbcBankDirectDebitImportUseCase smbcBankDirectDebitImportUseCase;

    private final SmbcCardImportUseCase smbcCardImportUseCase;

    private final JcbCardImportUseCase jcbCardImportUseCase;

    private static final long SMBC_BANK_TEMPLATE_ID = 1L;

    private static final long SMBC_CARD_TEMPLATE_ID = 4L;

    private static final long JCB_CARD_TEMPLATE_ID = 5L;

    @Override
    public BatchResult execute() {
        BatchResult result = new BatchResult("GmailImport");

        List<String> userIds = gmailCredentialRepository.findAll().stream()
                .map(GmailCredentialEntity::getUserId)
                .toList();

        for (String userId : userIds) {
            try {
                processUser(userId, result);
            } catch (Exception e) {
                log.error("ユーザーのGmail取り込みに失敗しました。userId={}", userId, e);
                result.add(BatchResultStatus.FAILURE);
            }
        }

        return result;
    }

    private void processUser(String userId, BatchResult result) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("ユーザーが見つかりません。userId=" + userId));

        List<Account> accounts = accountRepository.findByUserIdWithTemplate(userId);

        for (Account account : accounts) {
            if (account.getAccountTemplate() == null) continue;
            long templateId = account.getAccountTemplate().getId();

            if (templateId == SMBC_CARD_TEMPLATE_ID) {
                smbcCardImportUseCase.execute(user, account, result);
            } else if (templateId == JCB_CARD_TEMPLATE_ID) {
                jcbCardImportUseCase.execute(user, account, result);
            } else if (templateId == SMBC_BANK_TEMPLATE_ID) {
                smbcBankDepositImportUseCase.execute(user, account, result);
                smbcBankWithdrawalImportUseCase.execute(user, account, result);
                smbcBankDirectDebitImportUseCase.execute(user, account, result);
            }
        }
    }
}
