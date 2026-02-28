package com.xdfc.playground.adapter.in.web.rest.dto.mapper;

import com.xdfc.playground.adapter.in.web.rest.dto.account.ListingAccountDTO;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountEntityToListingAccountDTOMapper {
    ListingAccountDTO toDto(AccountEntity account);
}

