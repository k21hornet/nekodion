package com.konekokonekone.nekodion.ingest.controller;

import com.konekokonekone.nekodion.ingest.request.ForwardingConfirmationRequest;
import com.konekokonekone.nekodion.ingest.request.IngestEmailRequest;
import com.konekokonekone.nekodion.ingest.usecase.ForwardingConfirmationUseCase;
import com.konekokonekone.nekodion.ingest.usecase.IngestUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class IngestController {

    private final IngestUseCase ingestUseCase;

    private final ForwardingConfirmationUseCase forwardingConfirmationUseCase;

    @PostMapping("/inbound")
    public ResponseEntity<Void> ingestEmail(@RequestBody @Valid IngestEmailRequest request) {
        ingestUseCase.process(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forwarding-confirmation")
    public ResponseEntity<Void> forwardingConfirmation(@RequestBody @Valid ForwardingConfirmationRequest request) {
        forwardingConfirmationUseCase.process(request);
        return ResponseEntity.ok().build();
    }
}
