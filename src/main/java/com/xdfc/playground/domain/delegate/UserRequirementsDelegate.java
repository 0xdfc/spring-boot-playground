package com.xdfc.playground.domain.delegate;

import com.xdfc.playground.adapter.in.web.rest.dto.mapper.UserEntityMapper;
import com.xdfc.playground.domain.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
final public class UserRequirementsDelegate {
    @Autowired
    private UserEntityMapper mapper;

    @Autowired
    private UserService service;
}
