package ru.antush59.attestation03.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Услуги")
public class OptionRequestDto {

    @Schema(description = "Название услуги")
    private String name;

    @Schema(description = "Стоимость")
    private BigDecimal price;

}
