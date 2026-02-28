package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import com.xdfc.playground.adapter.out.persistence.jpa.embeddable.MonetaryAccountBalance;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Getter
final public class AccountEntity extends ExtendableUuidSuperEntity {

    /**
     * Made generic for use-cases where the account may be
     * something like a crypto address instead of an IBAN. The
     * MonetaryAccountBalance would be the flaw in this case.
     */
    @Column(nullable = false)
    @Email
    @NotBlank
    private String address;

    @Embedded
    private MonetaryAccountBalance balance;
}
