package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.in.web.rest.constant.AccountConstants;
import com.xdfc.playground.adapter.out.persistence.jpa.embeddable.MonetaryAccountBalance;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.CurrencyEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.CurrencyRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @NotNull
    public MonetaryAccountBalance make(@NotNull final String code) {
        @NotNull final CurrencyEntity currency = this
            .currencyRepository.findByCode(code);

        final MonetaryAccountBalance balance = new MonetaryAccountBalance();

        return balance.setCurrency(currency).setBalance(
            AccountConstants.AccountStartingBalance
        );
    }
}
