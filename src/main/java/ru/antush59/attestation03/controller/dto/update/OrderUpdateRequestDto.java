package ru.antush59.attestation03.controller.dto.update;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
public class OrderUpdateRequestDto {
    Long id;
    String customerLogin;
    OffsetDateTime creationTime;
    List<OptionUpdateRequestDto> options;
}
