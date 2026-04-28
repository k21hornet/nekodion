package com.konekokonekone.nekodion.batch.usecase;

import com.konekokonekone.nekodion.batch.runner.BatchResult;
import com.konekokonekone.nekodion.batch.runner.BatchResultStatus;
import com.konekokonekone.nekodion.category.service.CategoryMappingService;
import com.konekokonekone.nekodion.external.gmail.dto.GmailMessage;
import com.konekokonekone.nekodion.external.gmail.service.GmailClientService;
import com.konekokonekone.nekodion.external.gmail.service.GmailImportLogService;
import com.konekokonekone.nekodion.transaction.dto.TransactionRequestDto;
import com.konekokonekone.nekodion.transaction.entity.Account;
import com.konekokonekone.nekodion.transaction.enums.TransactionType;
import com.konekokonekone.nekodion.transaction.service.TransactionService;
import com.konekokonekone.nekodion.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmbcBankDirectDebitImportUseCase {

    private final GmailClientService gmailClientService;

    private final GmailImportLogService gmailLogService;

    private final TransactionService transactionService;

    private final CategoryMappingService categoryMappingService;

    private static final String GMAIL_QUERY_TEMPLATE = "subject:\"【三井住友銀行】口座引き落としの事前お知らせ\" after:%s";

    private static final int MAX_RESULTS = 100;

    private static final DateTimeFormatter QUERY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // 全角（１２３）・半角（123）どちらの番号にも対応
    private static final Pattern SECTION_PATTERN = Pattern.compile("◆明細[0-9０-９]+(.+?)(?=◆明細[0-9０-９]+|\\z)", Pattern.DOTALL);

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("引落金額[　 ]*：[　 ]*([\\d,.]+)円");

    private static final Pattern CONTENT_PATTERN = Pattern.compile("内容[　 ]*：[　 ]*(.+)");

    // 通知日時から年を取得: （2026年04月23日...
    private static final Pattern NOTIFY_YEAR_PATTERN = Pattern.compile("（(\\d{4})年");

    // 配信番号の先頭4桁がMMDD: 配信番号：　0424001244-0010
    private static final Pattern DELIVERY_DATE_PATTERN = Pattern.compile("配信番号[　 ]*：[　 ]*(\\d{2})(\\d{2})\\d+");

    public void execute(User user, Account account, BatchResult result) {
        var userId = user.getId();
        var query = GMAIL_QUERY_TEMPLATE.formatted(user.getCreatedAt().toLocalDate().format(QUERY_DATE_FORMAT));

        List<GmailMessage> messages;
        try {
            messages = gmailClientService.fetchMessages(userId, query, MAX_RESULTS);
        } catch (Exception e) {
            log.error("Gmail取得に失敗しました。userId={}", userId, e);
            result.add(BatchResultStatus.FAILURE);
            return;
        }

        var userStartedAt = user.getCreatedAt().atZone(ZoneId.of("Asia/Tokyo")).toInstant();
        for (GmailMessage message : messages) {
            if (message.getSentAt().toInstant().isBefore(userStartedAt)) {
                result.add(BatchResultStatus.SKIP);
                continue;
            }
            if (gmailLogService.isAlreadyImported(userId, message.getId())) {
                result.add(BatchResultStatus.SKIP);
                continue;
            }
            try {
                processMessage(userId, account, message);
                result.add(BatchResultStatus.SUCCESS);
            } catch (Exception e) {
                log.error("三井住友銀行引き落とし事前お知らせメールの処理に失敗しました。messageId={}", message.getId(), e);
                result.add(BatchResultStatus.FAILURE);
            }
        }
    }

    @Transactional
    public void processMessage(String userId, Account account, GmailMessage message) {
        var body = message.getBody().replace("\r\n", "\n");

        var debitDate = extractDebitDate(body);
        var items = extractItems(body);
        if (items.isEmpty()) throw new IllegalArgumentException("明細が見つかりません");

        String firstItemName = items.get(0).content;
        BigDecimal firstItemAmount = items.get(0).amount;

        for (DebitItem item : items) {
            var category = categoryMappingService.resolveCategory(userId, item.content, false);
            var dto = TransactionRequestDto.builder()
                    .accountId(account.getId())
                    .categoryId(category.getId())
                    .transactionType(TransactionType.EXPENSE.getCode())
                    .transactionName(item.content)
                    .amount(item.amount)
                    .transactionDateTime(debitDate)
                    .isAggregated(item.isAggregated)
                    .build();
            transactionService.createTransaction(userId, dto);
        }

        gmailLogService.record(userId, account.getId(), message.getId(), debitDate, firstItemAmount, firstItemName);
    }

    private LocalDateTime extractDebitDate(String body) {
        Matcher yearMatcher = NOTIFY_YEAR_PATTERN.matcher(body);
        if (!yearMatcher.find()) throw new IllegalArgumentException("通知年が見つかりません");
        int year = Integer.parseInt(yearMatcher.group(1));

        Matcher dateMatcher = DELIVERY_DATE_PATTERN.matcher(body);
        if (!dateMatcher.find()) throw new IllegalArgumentException("配信番号が見つかりません");
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
            if (!amountMatcher.find()) continue;
            BigDecimal amount = new BigDecimal(amountMatcher.group(1).replace(",", ""));

            Matcher contentMatcher = CONTENT_PATTERN.matcher(section);
            String content = contentMatcher.find() ? contentMatcher.group(1).trim() : "不明";

            // カードとあれば二重計上を避けるためisAggregated=false
            boolean isAggregated = !content.contains("カード");

            items.add(new DebitItem(amount, content, isAggregated));
        }
        return items;
    }

    private record DebitItem(BigDecimal amount, String content, boolean isAggregated) {}
}
