package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.AccountRepository;
import com.xdfc.playground.domain.dto.InternalAccountTransferDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Validated
@Service
public class TransferRunnerService {
    @Autowired
    private AccountRepository accountRepository;

     public void runTransfer(@Valid final InternalAccountTransferDto dto) {
        final AccountEntity source = dto.source(),
                destination = dto.destination();

        final BigDecimal amount = dto.amount();

        source.debit(amount);
        destination.credit(amount);

        this.accountRepository.saveAll(List.of(source, destination));
    }
}
