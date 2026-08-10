package com.konekokonekone.nekodion.ingest.service;

import com.konekokonekone.nekodion.category.service.CategoryMappingService;
import com.konekokonekone.nekodion.ingest.request.IngestEmailRequest;
import com.konekokonekone.nekodion.transaction.dto.TransactionRequestDto;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.enums.TransactionType;
import com.konekokonekone.nekodion.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmbcBankDepositImportService {

    private final TransactionService transactionService;

    private final CategoryMappingService categoryMappingService;

    private static final DateTimeFormatter BODY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final Pattern DATE_PATTERN = Pattern.compile("◇?入金日[　 ]*：[　 ]*(\\d{4}年\\d{2}月\\d{2}日)");

    private static final Pattern CONTENT_PATTERN = Pattern.compile("◇?内容[　 ]*：[　 ]*(.+)");

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("◇?金額[　 ]*：[　 ]*([\\d,.]+)(?:円|JPY)");

    private static final String SUBJECT_KEYWORD = "【三井住友銀行】振込入金のお知らせ";

    public boolean matches(IngestEmailRequest request) {
        return request.getSubject().contains(SUBJECT_KEYWORD);
    }

    @Transactional
    public void execute(String userId, Account account, IngestEmailRequest request) {
        var body = request.getText().replace("\r\n", "\n");

        var transactionDateTime = extractDate(body);
        var shopName = extractShopName(body);
        var amount = extractAmount(body);
        var category = categoryMappingService.resolveCategory(userId, shopName, true);

        var dto = TransactionRequestDto.builder()
                .accountId(account.getId())
                .categoryId(category.getId())
                .transactionType(TransactionType.NORMAL.getCode())
                .direction("IN")
                .transactionName(shopName)
                .amount(amount)
                .transactionDateTime(transactionDateTime)
                .isRead(false)
                .isDeletable(false)
                .build();

        transactionService.createTransaction(userId, dto);
    }

    private LocalDateTime extractDate(String body) {
        Matcher m = DATE_PATTERN.matcher(body);
        if (!m.find())
            throw new IllegalArgumentException("入金日が見つかりません");
        return LocalDate.parse(m.group(1), BODY_DATE_FORMAT).atStartOfDay();
    }

    private String extractShopName(String body) {
        Matcher m = CONTENT_PATTERN.matcher(body);
        return m.find() ? m.group(1).trim() : "不明";
    }

    private BigDecimal extractAmount(String body) {
        Matcher m = AMOUNT_PATTERN.matcher(body);
        if (!m.find())
            throw new IllegalArgumentException("金額が見つかりません");
        return new BigDecimal(m.group(1).replace(",", ""));
    }
}
