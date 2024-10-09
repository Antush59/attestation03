package ru.antush59.attestation03.service.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Order {

    private Long id;
    private String customerLogin;
    private OffsetDateTime creationTime;
    private List<Option> options;
}
