package com.konekokonekone.nekodion.transaction.service;

import com.konekokonekone.nekodion.transaction.entity.AccountTemplate;
import com.konekokonekone.nekodion.transaction.repository.AccountTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountTemplateService {

    private final AccountTemplateRepository accountTemplateRepository;

    /**
     * 口座テンプレート一覧取得
     *
     * @return 口座テンプレート一覧
     */
    public List<AccountTemplate> findAll() {
        return accountTemplateRepository.findAllByOrderByAccountTypeAscAccountNameAsc();
    }
}
