package com.xdfc.playground.adapter.out.persistence.jpa.repository;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends CrudRepository<AccountEntity, String> {
    // This could instead be written with a mangled prototype but it's
    // a tad ugly so I stuck to using a query
    @Query(
        "select a from AccountEntity a where a.owner = :owner and a.embeddedBalance.currency.code = :code"
    )
    Optional<AccountEntity> findByOwnerAndCurrencyCode(
        @NotNull final UserEntity owner,
        @NotNull final String code
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findWithLockByOwnerAndAddress(
        @NotNull final UserEntity owner,
        @UUID final String address
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findWithLockByAddress(@UUID final String add);

    Set<AccountEntity> findByOwner(@NotNull final UserEntity owner);
}
