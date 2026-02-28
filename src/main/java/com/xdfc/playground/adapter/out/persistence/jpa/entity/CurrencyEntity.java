package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import com.xdfc.playground.domain.enumerable.CurrencyCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyEntity extends ExtendableUuidSuperEntity {
    @Column(length = 36)
    @NotBlank
    private String code;

    public CurrencyEntity(CurrencyCode code) {
        this.setCode(code.toString());
    }
}
