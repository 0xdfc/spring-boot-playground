package com.xdfc.playground.adapter.in.web.rest.dto.mapper;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.in.web.rest.dto.user.ListingUserDTO;
import org.mapstruct.Mapper;

// The property componentModel enables DI from spring
@Mapper(componentModel="spring")
public interface UserEntityToListingUserDTOMapper {
    ListingUserDTO toDto(UserEntity user);
}
