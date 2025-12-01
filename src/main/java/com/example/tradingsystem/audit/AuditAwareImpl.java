package com.example.tradingsystem.audit;

import com.example.tradingsystem.dto.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
    @Autowired
    private AccountInfo accountInfo;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(accountInfo.username());
    }
}
