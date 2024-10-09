package ru.antush59.attestation03.controller.dto.response;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OptionResponseDto {
    Long id;
    String name;
    BigDecimal price;

}
