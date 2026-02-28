package com.xdfc.playground.adapter.out.persistence.jpa.repository;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.CurrencyEntity;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {
    CurrencyEntity findByCode(String code);
    Optional<CurrencyEntity> searchByCode(String code);
}
