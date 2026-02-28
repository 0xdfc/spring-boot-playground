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
    @Column(precision = 19, scale = 4, nullable = false)
    @NotNull
    private BigDecimal balance;

    @ManyToOne()
    private CurrencyEntity currency;

    public MonetaryAmount toMonetaryAmount() {
        return Money.of(this.balance, this.currency.getCode());
    }
}
