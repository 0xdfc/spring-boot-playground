package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.in.web.rest.dto.account.AccountTransferDto;
import com.xdfc.playground.adapter.in.web.rest.exception.NotFoundHttpException;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.AccountRepository;
import com.xdfc.playground.domain.dto.InternalAccountTransferDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class TransferEntrypointService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRunnerService runnerService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(
        @NotNull final UserEntity author,
        @Valid final AccountTransferDto transfer
    ) {
        final AccountEntity source = this.accountRepository
            .findWithLockByOwnerAndAddress(author, transfer.from())
            .orElseThrow(
                NotFoundHttpException::new
            );

        // In practice this could remain unlocked but then that would
        // allow breaching the upper bounds. Since we have low limits,
        // the breach wouldn't be much of an issue and would occur at
        // most once (breaching by 'one maximum' as per the dto).
        //
        // That said, I wanted to ensure there was nothing that could
        // be interpreted as a mistake or carelessness.
        final AccountEntity destination = this.accountRepository
            .findWithLockByAddress(transfer.to())
            .orElseThrow(NotFoundHttpException::new);

        final InternalAccountTransferDto runnableTransfer =
            new InternalAccountTransferDto(
                source,
                destination,
                transfer.amount()
            );

        this.runnerService.runTransfer(runnableTransfer);
    }
}
