package com.konekokonekone.nekodion.emailingest.repository;

import com.konekokonekone.nekodion.emailingest.entity.EmailForwardingConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailForwardingConfirmationRepository extends JpaRepository<EmailForwardingConfirmation, String> {
}
