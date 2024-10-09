package ru.antush59.attestation03.controller.dto.response;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
public class OrderResponseDto {
    Long id;
    String customerLogin;
    OffsetDateTime creationTime;
    List<OptionResponseDto> options;
}
