package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.dto.account.CreateAccountDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.account.ListingAccountDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.account.ListingAccountsDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.mapper.AccountEntityToListingAccountDTOMapper;
import com.xdfc.playground.adapter.in.web.rest.route.AccountControllerRoutes;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController()
@RequestMapping(AccountControllerRoutes.ROOT_ENDPOINT)
public class AccountController {
    @Autowired
    private AccountEntityToListingAccountDTOMapper mapper;

    @Autowired
    private AccountService service;

    @GetMapping
    public ListingAccountsDTO index(@AuthenticationPrincipal final UserEntity user) {
        return new ListingAccountsDTO(
            this.service.find(user)
                .stream()
                .map(account -> this.mapper.toDto(account))
                .collect(Collectors.toSet())
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListingAccountDTO create(
        @AuthenticationPrincipal final UserEntity user,
        @Valid @RequestBody final CreateAccountDTO dto
    ) {
        return this.mapper.toDto(this.service.upsert(user, dto));
    }
}
