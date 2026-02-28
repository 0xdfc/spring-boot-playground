package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.in.web.rest.dto.account.CreateAccountDTO;
import com.xdfc.playground.adapter.in.web.rest.exception.NotFoundHttpException;
import com.xdfc.playground.adapter.out.persistence.jpa.embeddable.MonetaryAccountBalance;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.AccountRepository;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
final public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @NotNull
    private AccountEntity insert(
        @NotNull final UserEntity user,
        @NotNull final CurrencyCode code
    ) {
        final MonetaryAccountBalance balance = this.balanceService
            .make(code);

        return this.repository.save(new AccountEntity(user, balance));
    }

    // Primarily used to avoid phantom reads (where another
    // record might be inserted and this transaction would
    // not see it). That said, this might be overkill since
    // we will pessimistically lock the user.
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @NotNull
    public AccountEntity upsert(
        @UUID final String id,
        @Valid final CreateAccountDTO dto
    ) {
        final UserEntity user = this.userService.findWithLockById(id)
            .orElseThrow(NotFoundHttpException::new);

        Optional<AccountEntity> existingAccount = this.repository
            .findByUserAndBalanceCurrencyCode(user, dto.code().toString());

        return existingAccount.orElse(this.insert(user, dto.code()));
    }
}
