package ru.antush59.attestation03.service.model;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
public class Option {

    private Long id;
    private String name;
    private BigDecimal price;

}
