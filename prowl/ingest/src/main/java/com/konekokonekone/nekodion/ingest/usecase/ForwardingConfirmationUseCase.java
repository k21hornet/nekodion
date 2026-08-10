package com.konekokonekone.nekodion.ingest.usecase;

import com.konekokonekone.nekodion.emailingest.service.EmailForwardingConfirmationService;
import com.konekokonekone.nekodion.ingest.request.ForwardingConfirmationRequest;
import com.konekokonekone.nekodion.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional
public class ForwardingConfirmationUseCase {

    private final UserService userService;

    private final EmailForwardingConfirmationService emailForwardingConfirmationService;

    public void process(ForwardingConfirmationRequest request) {
        var user = userService.findByEmailForwardToken(request.getToken());
        var receivedAt = LocalDateTime.ofInstant(request.getReceivedAt(), ZoneId.systemDefault());

        emailForwardingConfirmationService.saveOrUpdate(
                user.getId(),
                request.getFrom(),
                request.getSubject(),
                request.getText(),
                receivedAt
        );
    }
}
