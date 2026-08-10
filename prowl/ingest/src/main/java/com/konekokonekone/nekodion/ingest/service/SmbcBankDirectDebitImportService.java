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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmbcBankDirectDebitImportService {

    private final TransactionService transactionService;

    private final CategoryMappingService categoryMappingService;

    // 全角（１２３）・半角（123）どちらの番号にも対応
    private static final Pattern SECTION_PATTERN = Pattern.compile("◆明細[0-9０-９]+(.+?)(?=◆明細[0-9０-９]+|\\z)",
            Pattern.DOTALL);

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("引落金額[　 ]*：[　 ]*([\\d,.]+)円");

    private static final Pattern CONTENT_PATTERN = Pattern.compile("内容[　 ]*：[　 ]*(.+)");

    // 通知日時から年を取得: （2026年04月23日...
    private static final Pattern NOTIFY_YEAR_PATTERN = Pattern.compile("（(\\d{4})年");

    // 配信番号の先頭4桁がMMDD: 配信番号： 0424001244-0010
    private static final Pattern DELIVERY_DATE_PATTERN = Pattern.compile("配信番号[　 ]*：[　 ]*(\\d{2})(\\d{2})\\d+");

    private static final String SUBJECT_KEYWORD = "【三井住友銀行】口座引き落としの事前お知らせ";

    public boolean matches(IngestEmailRequest request) {
        return request.getSubject().contains(SUBJECT_KEYWORD);
    }

    @Transactional
    public void execute(String userId, Account account, IngestEmailRequest request) {
        var body = request.getText().replace("\r\n", "\n");

        var debitDate = extractDebitDate(body);
        var items = extractItems(body);
        if (items.isEmpty())
            throw new IllegalArgumentException("明細が見つかりません");

        for (DebitItem item : items) {
            var category = categoryMappingService.resolveCategory(userId, item.content, false);
            var dto = TransactionRequestDto.builder()
                    .accountId(account.getId())
                    .categoryId(category.getId())
                    .transactionType(TransactionType.NORMAL.getCode())
                    .direction("OUT")
                    .transactionName(item.content)
                    .amount(item.amount)
                    .transactionDateTime(debitDate)
                    .isAggregated(item.isAggregated)
                    .isRead(false)
                    .isDeletable(false)
                    .build();
            transactionService.createTransaction(userId, dto);
        }
    }

    private LocalDateTime extractDebitDate(String body) {
        Matcher yearMatcher = NOTIFY_YEAR_PATTERN.matcher(body);
        if (!yearMatcher.find())
            throw new IllegalArgumentException("通知年が見つかりません");
        int year = Integer.parseInt(yearMatcher.group(1));

        Matcher dateMatcher = DELIVERY_DATE_PATTERN.matcher(body);
        if (!dateMatcher.find())
            throw new IllegalArgumentException("配信番号が見つかりません");
        int month = Integer.parseInt(dateMatcher.group(1));
        int day = Integer.parseInt(dateMatcher.group(2));

        return LocalDate.of(year, month, day).atStartOfDay();
    }

    private List<DebitItem> extractItems(String body) {
        List<DebitItem> items = new ArrayList<>();
        Matcher sectionMatcher = SECTION_PATTERN.matcher(body);
        while (sectionMatcher.find()) {
            String section = sectionMatcher.group(1);

            Matcher amountMatcher = AMOUNT_PATTERN.matcher(section);
            if (!amountMatcher.find())
                continue;
            BigDecimal amount = new BigDecimal(amountMatcher.group(1).replace(",", ""));

            Matcher contentMatcher = CONTENT_PATTERN.matcher(section);
            String content = contentMatcher.find() ? contentMatcher.group(1).trim() : "不明";

            // カードとあれば二重計上を避けるためisAggregated=false
            boolean isAggregated = !content.contains("カード");

            items.add(new DebitItem(amount, content, isAggregated));
        }
        return items;
    }

    private record DebitItem(BigDecimal amount, String content, boolean isAggregated) {
    }
}
