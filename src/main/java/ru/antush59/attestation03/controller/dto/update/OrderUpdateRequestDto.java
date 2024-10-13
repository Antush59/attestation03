package ru.antush59.attestation03.controller.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
@Schema(description = "Заказ")
public class OrderUpdateRequestDto {

    @Schema(description = "Уникальный идентификатор")
    Long id;

    @Schema(description = "Логин пользователя")
    String customerLogin;

    @Schema(description = "Дата создания заказа")
    OffsetDateTime creationTime;

    @Schema(description = "Выбранные услуги")
    List<OptionUpdateRequestDto> options;
}
