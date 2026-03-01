package com.xdfc.playground.adapter.out.persistence.jpa.embeddable;

import com.xdfc.playground.adapter.out.persistence.jpa.entity.CurrencyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

@Accessors(chain = true)
@Getter
@Setter
@Embeddable
final public class MonetaryAccountBalance {
    @NotNull
    private BigDecimal balance;

    @ManyToOne()
    private CurrencyEntity currency;

    public MonetaryAmount toMonetaryAmount() {
        return Money.of(this.balance, this.currency.getCode());
    }

    /**
     * It is the caller's responsibility to ensure that
     * the balance will not go under zero.
     *
     * @param sum expected to be a validated big decimal
     */
    public void debit(@NotNull final BigDecimal sum) {
        this.setBalance(this.getBalance().subtract(sum));
    }


    /**
     * It is the caller's responsibility to ensure that
     * the balance will not go beyond the AccountConstants
     * .MaximumAccountBalance limit.
     *
     * @param sum expected to be a validated big decimal
     */
    public void credit(@NotNull final BigDecimal sum) {
        this.setBalance(this.getBalance().add(sum));
    }
}
