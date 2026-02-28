package com.xdfc.playground.adapter.out.persistence.jpa.repository;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String>,
        PagingAndSortingRepository<UserEntity, String> {
    // Lock added for a future case where usernames may be updated
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserEntity> findWithLockByUsername(final String username);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<UserEntity> findByUsername(final String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserEntity> findWithLockById(
        @Param("id") final String id
    );
}
