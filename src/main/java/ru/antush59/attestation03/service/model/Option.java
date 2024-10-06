package ru.antush59.attestation03.service.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Option {

    private Long id;
    private String name;
    private BigDecimal price;

}
