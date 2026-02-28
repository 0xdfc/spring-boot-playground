package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import com.xdfc.playground.adapter.out.persistence.jpa.embeddable.MonetaryAccountBalance;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@Entity
final public class AccountEntity extends ExtendableUuidSuperEntity {

    /**
     * Made generic for use-cases where the account may be
     * something like a crypto address instead of an IBAN. The
     * MonetaryAccountBalance would be the flaw in this case.
     */
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String address;

    @Embedded
    private MonetaryAccountBalance balance;

    @ManyToOne
    // Although everyone online seems to use the JoinColumn, oddly
    // some of the docs I found in jakarta show examples without leading
    // me to believe there might be inference which takes care of it.
    private UserEntity owner;

    public AccountEntity(
        @NotNull final UserEntity owner,
        @NotNull final MonetaryAccountBalance balance
    ) {
        this.setOwner(owner).setBalance(balance);
    }
}
