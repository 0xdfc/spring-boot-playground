package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.in.web.rest.dto.account.CreateAccountDTO;
import com.xdfc.playground.adapter.out.persistence.jpa.embeddable.MonetaryAccountBalance;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.AccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Set;

@Service
@Validated
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private BalanceService balanceService;

    /**
     * ? Susceptible to duplicate creation and has absolutely
     * ? no protection. Exposed for the sake of use within test
     * ? factories. Maybe there is a better way?
     */
    @NotNull public AccountEntity insert(
        @NotNull final UserEntity user,
        @NotNull final String code
    ) {
        final MonetaryAccountBalance balance = this.balanceService
            .make(code);

        return this.repository.save(new AccountEntity(user, balance));
    }

    @Transactional()
    @NotNull public AccountEntity upsert(
        @NotNull final UserEntity user,
        @Valid final CreateAccountDTO dto
    ) {
        Optional<AccountEntity> existingAccount = this.repository
            .findByOwnerAndCurrencyCode(user, dto.code());

        return existingAccount.orElse(this.insert(user, dto.code()));
    }

    public Set<AccountEntity> find(@NotNull final UserEntity user) {
        return this.repository.findByOwner(user);
    }

    public Optional<AccountEntity> find(@NotNull final String id) {
        return this.repository.findById(id);
    }

    public AccountEntity save(@NotNull final AccountEntity account) {
        return this.repository.save(account);
    }
}
