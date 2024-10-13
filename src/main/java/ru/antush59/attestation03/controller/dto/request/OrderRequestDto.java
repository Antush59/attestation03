package ru.antush59.attestation03.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Заказ")
public class OrderRequestDto {

    @Schema(description = "Логин пользователя")
    private String customerLogin;

    @Schema(description = "Выбранные услуги")
    private List<String> options;
}
