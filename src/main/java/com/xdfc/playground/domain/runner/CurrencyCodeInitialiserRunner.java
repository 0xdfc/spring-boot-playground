package com.xdfc.playground.domain.runner;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.CurrencyEntity;
import com.xdfc.playground.adapter.out.persistence.jpa.repository.CurrencyRepository;
import com.xdfc.playground.domain.enumerable.CurrencyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CurrencyCodeInitialiserRunner implements CommandLineRunner {
    @Autowired
    private CurrencyRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(CurrencyCode.values())
            .filter(
                (code) -> this.repository.searchByCode(code.toString())
                    .isEmpty()
            )
            .forEach(
                (code) -> this.repository.save(
                    new CurrencyEntity(code)
                )
            );
    }
}
