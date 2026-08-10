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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmbcCardImportService {

    private final TransactionService transactionService;

    private final CategoryMappingService categoryMappingService;

    private static final DateTimeFormatter BODY_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    // 自動転送等、メール本文が改変されない場合の本来のラベル付き形式
    private static final Pattern LABELED_DATE_PATTERN = Pattern.compile("◇?利用日：(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2})");

    private static final Pattern LABELED_SHOP_PATTERN = Pattern.compile("◇?利用先：(.+)");

    private static final Pattern LABELED_AMOUNT_PATTERN = Pattern.compile("◇?利用金額：([\\d,.]+)(?:円|JPY)");

    // Gmailの手動転送でラベルが失われ、日付行の次の行に「店名 金額円」が並ぶ形式（例: OZEKI（買物） 924円）
    private static final Pattern FLAT_DATE_PATTERN = Pattern.compile("ご利用日時[　 ]*：[　 ]*(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2})");

    private static final Pattern FLAT_SHOP_AMOUNT_PATTERN = Pattern.compile(
            "ご利用日時[　 ]*：[　 ]*\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}\\n(.+?)[　 ]+([\\d,.]+)円");

    private static final String SUBJECT_KEYWORD = "ご利用のお知らせ【三井住友カード】";

    public boolean matches(IngestEmailRequest request) {
        return request.getSubject().contains(SUBJECT_KEYWORD);
    }

    @Transactional
    public void execute(String userId, Account account, IngestEmailRequest request) {
        var body = request.getText().replace("\r\n", "\n");

        var transactionDateTime = extractDate(body);
        var shopName = extractShopName(body);
        var amount = extractAmount(body);
        var category = categoryMappingService.resolveCategory(userId, shopName, false);

        var dto = TransactionRequestDto.builder()
                .accountId(account.getId())
                .categoryId(category.getId())
                .transactionType(TransactionType.NORMAL.getCode())
                .direction("OUT")
                .transactionName(shopName)
                .amount(amount)
                .transactionDateTime(transactionDateTime)
                .isRead(false)
                .isDeletable(false)
                .build();

        transactionService.createTransaction(userId, dto);
    }

    private LocalDateTime extractDate(String body) {
        Matcher labeled = LABELED_DATE_PATTERN.matcher(body);
        if (labeled.find())
            return LocalDateTime.parse(labeled.group(1), BODY_DATETIME_FORMAT);

        Matcher flat = FLAT_DATE_PATTERN.matcher(body);
        if (flat.find())
            return LocalDateTime.parse(flat.group(1), BODY_DATETIME_FORMAT);

        throw new IllegalArgumentException("利用日時が見つかりません");
    }

    private String extractShopName(String body) {
        Matcher labeled = LABELED_SHOP_PATTERN.matcher(body);
        if (labeled.find())
            return labeled.group(1).trim();

        Matcher flat = FLAT_SHOP_AMOUNT_PATTERN.matcher(body);
        return flat.find() ? flat.group(1).trim() : "不明";
    }

    private BigDecimal extractAmount(String body) {
        Matcher labeled = LABELED_AMOUNT_PATTERN.matcher(body);
        if (labeled.find())
            return new BigDecimal(labeled.group(1).replace(",", ""));

        Matcher flat = FLAT_SHOP_AMOUNT_PATTERN.matcher(body);
        if (flat.find())
            return new BigDecimal(flat.group(2).replace(",", ""));

        throw new IllegalArgumentException("利用金額が見つかりません");
    }
}
