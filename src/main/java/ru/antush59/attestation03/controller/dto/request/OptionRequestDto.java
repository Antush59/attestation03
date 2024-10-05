package ru.antush59.attestation03.controller.dto.request;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OptionRequestDto {
    String name;
    BigDecimal price;

}
