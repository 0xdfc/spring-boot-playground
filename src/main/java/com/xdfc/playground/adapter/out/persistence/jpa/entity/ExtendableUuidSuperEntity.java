package com.xdfc.playground.adapter.out.persistence.jpa.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
abstract public class ExtendableUuidSuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}
