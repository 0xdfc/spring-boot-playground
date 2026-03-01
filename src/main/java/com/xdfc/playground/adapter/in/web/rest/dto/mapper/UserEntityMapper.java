package com.xdfc.playground.adapter.in.web.rest.dto.mapper;

import com.xdfc.playground.adapter.in.web.rest.dto.user.CreateUserDTO;
import com.xdfc.playground.adapter.in.web.rest.dto.user.ListingUserDTO;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity toUser(CreateUserDTO user);

    ListingUserDTO toDto(UserEntity user);
}
