package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.mapper.AccountDetailResponseMapper;
import com.konekokonekone.nekodion.api.mapper.AccountSummaryResponseMapper;
import com.konekokonekone.nekodion.api.mapper.AccountTemplateResponseMapper;
import com.konekokonekone.nekodion.api.response.AccountDetailResponse;
import com.konekokonekone.nekodion.api.response.AccountTemplateResponse;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.enums.AccountType;
import com.konekokonekone.nekodion.transaction.service.AccountService;
import com.konekokonekone.nekodion.api.response.AccountSummaryResponse;
import com.konekokonekone.nekodion.transaction.service.AccountTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountService accountService;

    private final AccountTemplateService accountTemplateService;

    private final AccountSummaryResponseMapper accountSummaryResponseMapper;

    private final AccountTemplateResponseMapper accountTemplateResponseMapper;

    private final AccountDetailResponseMapper accountDetailResponseMapper;

    /**
     * ユーザーの口座一覧取得（口座種別ごとにグルーピング、残高付き）
     *
     * @param userId ユーザーID
     * @return 口座種別ごとの口座一覧
     */
    public List<AccountSummaryResponse> getAccounts(String userId) {
        var accounts = accountService.findByUserId(userId);

        // 口座種別Map<口座種別, 口座>
        var grouped = accounts.stream()
                .collect(Collectors.groupingBy(
                        Account::getAccountType,
                        () -> new TreeMap<>(Comparator.comparingInt(AccountType::ordinal)), // 口座種別でソート
                        Collectors.toList()
                ));
        return grouped.entrySet().stream()
                .map(e -> {
                    var accountType = e.getKey();
                    var accountItems = e.getValue().stream()
                            .map(accountSummaryResponseMapper::toResponse)
                            .toList();
                    return AccountSummaryResponse.builder()
                            .accountType(accountType.getAccountTypeName())
                            .accounts(accountItems)
                            .build();
                }).toList();
    }

    /**
     * 口座テンプレート一覧取得
     *
     * @return 口座テンプレート一覧
     */
    public List<AccountTemplateResponse> getAccountTemplates() {
        var accountTemplates = accountTemplateService.findAll();
        return accountTemplates.stream()
                .map(accountTemplateResponseMapper::toResponse)
                .toList();
    }

    /**
     * 口座詳細取得
     *
     * @param id 口座ID
     * @param userId ユーザーID
     * @return 口座詳細
     */
    public AccountDetailResponse getAccount(Long id, String userId) {
        var account = accountService.findByIdAndUserId(id, userId);
        return accountDetailResponseMapper.toResponse(account);
    }
}
