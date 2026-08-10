package com.konekokonekone.nekodion.transaction.service;

import com.konekokonekone.nekodion.support.exception.EntityExistException;
import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.enums.AccountType;
import com.konekokonekone.nekodion.transaction.repository.AccountRepository;
import com.konekokonekone.nekodion.transaction.repository.AccountTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountTemplateRepository accountTemplateRepository;

    private final TransactionService transactionService;

    /**
     * ユーザーの口座一覧取得（未分類口座は除外）
     *
     * @param userId ユーザーID
     * @return 口座一覧
     */
    public List<Account> findByUserId(String userId) {
        return accountRepository.findByUserIdWithTransactions(userId);
    }

    public List<Account> findByUserIdWithTemplate(String userId) {
        return accountRepository.findByUserIdWithTemplate(userId);
    }

    /**
     * 口座詳細取得
     *
     * @param id     口座ID
     * @param userId ユーザーID
     * @return 口座
     */
    public Account findByIdAndUserId(Long id, String userId) {
        return accountRepository.findByIdAndUserIdWithTransactions(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座Id[%d]", id)));
    }

    /**
     * 口座作成
     *
     * @param userId            ユーザーID
     * @param accountTypeString 口座種別文字列
     * @param accountTemplateId 口座テンプレートID（任意）
     * @param accountName       口座名
     * @param balance           残高（任意、null または 0 の場合は残高調整取引を作成しない）
     * @param closingDay        締日（任意、クレカ口座のみ使用）
     */
    public void createAccount(String userId, String accountTypeString, Long accountTemplateId, String accountName, BigDecimal balance, Integer closingDay) {
        var exists = accountRepository.existsByUserIdAndAccountName(userId, accountName);
        if (exists) {
            throw new EntityExistException(String.format("既に登録済みです。口座名[%s]", accountName));
        }

        var account = new Account();
        account.setAccountType(AccountType.codeOf(accountTypeString));
        account.setAccountName(accountName);
        account.setUserId(userId);
        account.setClosingDay(closingDay);

        if (AccountType.UNCATEGORIZED.equals(account.getAccountType())) {
            throw new IllegalArgumentException("未分類口座は作成できません。");
        }

        if (accountTemplateId != null) {
            var template = accountTemplateRepository.findById(accountTemplateId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("口座テンプレートが見つかりません。口座テンプレートId[%d]", accountTemplateId)));
            if (!template.getAccountType().equals(account.getAccountType())) {
                throw new IllegalArgumentException(String.format("口座テンプレートの口座種別が一致しません。口座テンプレートId[%d]", accountTemplateId));
            }
            account.setAccountTemplate(template);
        }

        accountRepository.save(account);

        if (balance != null && balance.signum() != 0) {
            transactionService.createAdjustmentTransaction(account, userId, balance, "初期残高");
        }
    }

    /**
     * 口座更新
     *
     * @param id                口座ID
     * @param userId            ユーザーID
     * @param accountTypeString 口座種別文字列
     * @param accountTemplateId 口座テンプレートID（任意）
     * @param accountName       口座名
     * @param closingDay        締日（任意、クレカ口座のみ使用）
     */
    public void updateAccount(Long id, String userId, String accountTypeString, Long accountTemplateId, String accountName, Integer closingDay) {
        var account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座Id[%d]", id)));

        if (!account.getAccountName().equals(accountName)) {
            var exists = accountRepository.existsByUserIdAndAccountName(userId, accountName);
            if (exists) {
                throw new EntityExistException(String.format("既に登録済みです。口座名[%s]", accountName));
            }
        }

        account.setAccountType(AccountType.codeOf(accountTypeString));
        account.setAccountName(accountName);
        account.setClosingDay(closingDay);

        if (AccountType.UNCATEGORIZED.equals(account.getAccountType())) {
            throw new IllegalArgumentException("未分類口座は作成できません。");
        }

        if (accountTemplateId != null) {
            var template = accountTemplateRepository.findById(accountTemplateId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("口座テンプレートが見つかりません。口座テンプレートId[%d]", accountTemplateId)));
            if (!template.getAccountType().equals(account.getAccountType())) {
                throw new IllegalArgumentException(String.format("口座テンプレートの口座種別が一致しません。口座テンプレートId[%d]", accountTemplateId));
            }
            account.setAccountTemplate(template);
        } else {
            account.setAccountTemplate(null);
        }

        accountRepository.save(account);
    }

    /**
     * 口座残高編集
     * <p>
     * 現在の残高との差分を残高調整取引として記録する。
     *
     * @param id      口座ID
     * @param userId  ユーザーID
     * @param balance 編集後の残高
     */
    public void updateAccountBalance(Long id, String userId, BigDecimal balance) {
        var account = accountRepository.findByIdAndUserIdWithTransactions(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座Id[%d]", id)));

        var diff = balance.subtract(account.calculateBalance());
        if (diff.signum() != 0) {
            transactionService.createAdjustmentTransaction(account, userId, diff, "残高調整");
        }
    }

    /**
     * 未分類口座作成（ユーザー作成時に自動生成）
     *
     * @param userId ユーザーID
     */
    public void createUncategorizedAccount(String userId) {
        var account = new Account();
        account.setAccountType(AccountType.UNCATEGORIZED);
        account.setAccountName("未分類");
        account.setUserId(userId);

        accountRepository.save(account);
    }

    /**
     * 口座削除
     *
     * @param id     口座ID
     * @param userId ユーザーID
     */
    public void deleteAccount(Long id, String userId) {
        var account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座Id[%d]", id)));
        accountRepository.delete(account);
    }
}
