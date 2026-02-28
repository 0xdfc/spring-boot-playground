package com.xdfc.playground.adapter.out.web.rest.dto.mapper;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.web.rest.dto.ListingUserDTO;
import org.mapstruct.Mapper;

// The property componentModel enables DI from spring
@Mapper(componentModel="spring")
public interface UserEntityToListingUserDTOMapper {
    ListingUserDTO toDto(UserEntity user);
}
