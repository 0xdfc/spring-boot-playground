package com.xdfc.playground.adapter.in.web.rest.dto.mapper;

import com.xdfc.playground.adapter.in.web.rest.dto.CreateUserDTO;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateUserDTOToUserEntityMapper {
    UserEntity toUser(CreateUserDTO user);
}
