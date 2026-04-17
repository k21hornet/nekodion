package com.konekokonekone.nekodion.transaction.repository;

import com.konekokonekone.nekodion.transaction.entity.AccountTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountTemplateRepository extends JpaRepository<AccountTemplate, Long> {

    List<AccountTemplate> findAllByOrderByAccountTypeAscAccountNameAsc();
}
