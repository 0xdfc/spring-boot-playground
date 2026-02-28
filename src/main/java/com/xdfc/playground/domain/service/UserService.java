package com.xdfc.playground.domain.service;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    public Optional<UserEntity> find(final String id) {
        return this.repository.findById(id);
    }

    public Optional<UserEntity> findByUsername(final String username) {
        return this.repository.findByUsername(username);
    }

    public UserEntity save(final UserEntity user) {
        return this.repository.save(user);
    }

    public void delete(final String id) {
        this.repository.deleteById(id);
    }
}
