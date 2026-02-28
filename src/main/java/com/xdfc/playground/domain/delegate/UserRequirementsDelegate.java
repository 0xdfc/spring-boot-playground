package com.xdfc.playground.domain.delegate;

import com.xdfc.playground.adapter.in.web.rest.dto.mapper.CreateUserDTOToUserEntityMapper;
import com.xdfc.playground.adapter.in.web.rest.dto.mapper.UserEntityToListingUserDTOMapper;
import com.xdfc.playground.domain.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
final public class UserRequirementsDelegate {
    @Autowired
    private UserEntityToListingUserDTOMapper listingMapper;

    @Autowired
    private CreateUserDTOToUserEntityMapper creationMapper;

    @Autowired
    private UserService service;
}
