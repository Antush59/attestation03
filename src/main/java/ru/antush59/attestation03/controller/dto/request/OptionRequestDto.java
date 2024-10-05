package ru.antush59.attestation03.controller.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OptionRequestDto {
    private String name;
    private BigDecimal price;

}
