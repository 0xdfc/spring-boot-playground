package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import com.xdfc.playground.adapter.out.persistence.jpa.embeddable.MonetaryAccountBalance;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"owner_id", "currency_id"})
})
final public class AccountEntity extends ExtendableUuidSuperEntity {

    /**
     * Made generic for use-cases where the account may be
     * something like a crypto address instead of an IBAN. The
     * MonetaryAccountBalance would be the flaw in this case.
     */
    @Column(nullable = false, unique = true)
    private String address;

    @Embedded
    @Delegate
    private MonetaryAccountBalance embeddedBalance;

    @ManyToOne
    // Although everyone online seems to use the JoinColumn, oddly
    // some of the docs I found in jakarta show examples without, leading
    // me to believe there might be inference which takes care of it.
    private UserEntity owner;

    public AccountEntity(
        @NotNull final UserEntity owner,
        @NotNull final MonetaryAccountBalance balance
    ) {
        this.setOwner(owner).setEmbeddedBalance(balance);
    }

    @PrePersist
    private void creating() {
        this.setAddress(UUID.randomUUID().toString());
    }

    public String getCurrencyId() {
        return this.getCurrency().getId();
    }
}
