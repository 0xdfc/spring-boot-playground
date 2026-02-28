package com.xdfc.playground.factory;

import com.xdfc.playground.adapter.in.web.rest.dto.user.CreateUserDTO;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.domain.delegate.UserRequirementsDelegate;
import com.xdfc.playground.generator.FakeDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

final public class TestUserFactory {
    @Autowired
    private UserRequirementsDelegate delegate;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FakeDataGenerator generator;

    public UserEntity createAndPersist(String email, String password) {
        final CreateUserDTO dto = CreateUserDTO.make(email, password);

        final UserEntity user = this.delegate.getCreationMapper()
                .toUser(dto);

        user.setPassword(this.encoder.encode(user.getPassword()));

        return this.delegate.getService().save(user);
    }

    public UserEntity createAndPersist() {
        return this.createAndPersist(
            this.generator.generateValidEmailAddress(),
            this.generator.generateValidUserPassword()
        );
    }
}
