package com.xdfc.playground.adapter.out.persistence.jpa.repository;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity, String> { }
