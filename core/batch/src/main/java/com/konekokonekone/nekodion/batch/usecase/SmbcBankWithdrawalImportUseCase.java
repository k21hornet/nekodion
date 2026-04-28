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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmbcBankWithdrawalImportUseCase {

    private final GmailClientService gmailClientService;

    private final GmailImportLogService gmailLogService;

    private final TransactionService transactionService;

    private final CategoryMappingService categoryMappingService;

    private static final String GMAIL_QUERY_TEMPLATE = "subject:\"【三井住友銀行】口座出金のお知らせ\" after:%s";

    private static final int MAX_RESULTS = 100;

    private static final DateTimeFormatter QUERY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static final DateTimeFormatter BODY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final Pattern DATE_PATTERN = Pattern.compile("出金日[　 ]*：[　 ]*(\\d{4}年\\d{2}月\\d{2}日)");

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("出金額[　 ]*：[　 ]*([\\d,.]+)円");

    private static final Pattern CONTENT_PATTERN = Pattern.compile("内容[　 ]*：[　 ]*(.+)");

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
                log.error("三井住友銀行口座出金メールの処理に失敗しました。messageId={}", message.getId(), e);
                result.add(BatchResultStatus.FAILURE);
            }
        }
    }

    @Transactional
    public void processMessage(String userId, Account account, GmailMessage message) {
        var body = message.getBody().replace("\r\n", "\n");

        var transactionDateTime = extractDate(body);
        var shopName = extractContent(body);
        var amount = extractAmount(body);
        var category = categoryMappingService.resolveCategory(userId, shopName, false);

        var dto = TransactionRequestDto.builder()
                .accountId(account.getId())
                .categoryId(category.getId())
                .transactionType(TransactionType.EXPENSE.getCode())
                .transactionName(shopName)
                .amount(amount)
                .transactionDateTime(transactionDateTime)
                .build();

        transactionService.createTransaction(userId, dto);
        gmailLogService.record(userId, account.getId(), message.getId(), transactionDateTime, amount, shopName);
    }

    private LocalDateTime extractDate(String body) {
        Matcher m = DATE_PATTERN.matcher(body);
        if (!m.find()) throw new IllegalArgumentException("出金日が見つかりません");
        return LocalDate.parse(m.group(1), BODY_DATE_FORMAT).atStartOfDay();
    }

    private String extractContent(String body) {
        Matcher m = CONTENT_PATTERN.matcher(body);
        return m.find() ? m.group(1).trim() : "不明";
    }

    private BigDecimal extractAmount(String body) {
        Matcher m = AMOUNT_PATTERN.matcher(body);
        if (!m.find()) throw new IllegalArgumentException("出金額が見つかりません");
        return new BigDecimal(m.group(1).replace(",", ""));
    }
}
