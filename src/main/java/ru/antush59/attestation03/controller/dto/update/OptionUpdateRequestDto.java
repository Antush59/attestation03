package ru.antush59.attestation03.controller.dto.update;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OptionUpdateRequestDto {
    Long id;
    String name;
    BigDecimal price;

}
