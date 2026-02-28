package com.xdfc.playground.factory;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import com.xdfc.playground.domain.service.AccountService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class TestAccountFactory {
    @Autowired
    private AccountService accountService;

    public AccountEntity createAndPersist(@NotNull final UserEntity user) {
        return this.accountService.insert(user, CurrencyCode.AED.toString());
    }

    public AccountEntity createAndPersist(
        @NotNull final UserEntity user,
        @NotNull final BigDecimal balance
    ) {
        final AccountEntity account = this.createAndPersist(user);

        account.setBalance(balance);

        return this.accountService.save(account);
    }

    public List<AccountEntity> createAndPersistMany(
        @NotNull final List<UserEntity> users
    ) {
        return users.stream().map(this::createAndPersist).toList();
    }
}
