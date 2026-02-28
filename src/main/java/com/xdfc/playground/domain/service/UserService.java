package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repository;

    public Page<UserEntity> all(final Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public Optional<UserEntity> find(@UUID final String id) {
        return this.repository.findById(id);
    }

    public Optional<UserEntity> findByUsername(@Email final String username) {
        return this.repository.findByUsername(username);
    }

    public Optional<UserEntity> findWithLockByUsername(
        @Email final String username
    ) {
        return this.repository.findWithLockByUsername(
            username
        );
    }

    public Optional<UserEntity> findWithLockById(@UUID final String id) {
        return this.repository.findWithLockById(id);
    }

    public UserEntity save(@NotNull final UserEntity user) {
        return this.repository.save(user);
    }

    public void delete(@UUID final String id) {
        this.repository.deleteById(id);
    }
}
