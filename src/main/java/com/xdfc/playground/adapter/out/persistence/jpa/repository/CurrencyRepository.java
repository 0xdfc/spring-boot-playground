package com.xdfc.playground.adapter.out.persistence.jpa.repository;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.CurrencyEntity;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {
    public CurrencyEntity findByCode(CurrencyCode code);
}
