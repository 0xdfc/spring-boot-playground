package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.dto.account.AccountTransferDto;
import com.xdfc.playground.adapter.in.web.rest.route.TransferControllerRoutes;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.service.TransferEntrypointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(TransferControllerRoutes.ROOT_ENDPOINT)
public class TransferController {
    @Autowired
    private TransferEntrypointService transferEntrypointService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void transfer(
        @AuthenticationPrincipal final UserEntity user,
        @Valid @RequestBody final AccountTransferDto transfer
    ) {
        this.transferEntrypointService.transfer(user, transfer);
    }
}
