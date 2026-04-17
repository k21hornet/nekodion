package com.konekokonekone.nekodion.transaction.service;

import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.repository.AccountRepository;
import com.konekokonekone.nekodion.transaction.repository.AccountTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountTemplateRepository accountTemplateRepository;

    /**
     * ユーザーの口座一覧取得
     *
     * @param userId ユーザーID
     * @return 口座一覧
     */
    public List<Account> findByUserId(String userId) {
        return accountRepository.findByUserIdWithTransactions(userId);
    }

    /**
     * 口座詳細取得
     *
     * @param id 口座ID
     * @param userId ユーザーID
     * @return 口座
     */
    public Account findByIdAndUserId(Long id, String userId) {
        return accountRepository.findByIdAndUserIdWithTransactions(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座Id[%d]", id)));
    }
}
