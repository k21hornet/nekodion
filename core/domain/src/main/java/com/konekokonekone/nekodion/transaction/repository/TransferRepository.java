package com.konekokonekone.nekodion.transaction.repository;

import com.konekokonekone.nekodion.transaction.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
