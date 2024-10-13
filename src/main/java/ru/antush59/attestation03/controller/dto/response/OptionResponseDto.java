package ru.antush59.attestation03.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Schema(description = "Услуги")
public class OptionResponseDto {

    @Schema(description = "Уникальный идентификатор")
    Long id;

    @Schema(description = "Название услуги")
    String name;

    @Schema(description = "Стоимость")
    BigDecimal price;

}
