package ru.antush59.attestation03.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.Set;

@Value
@Schema(description = "Пользователь")
public class CustomerResponseDto {

    @Schema(description = "Уникальный логин")
    String login;

    @Schema(description = "Имя пользователя")
    String name;

    @Schema(description = "Номер телефона")
    String phoneNumber;

    @Schema(description = "Модель автомобиля")
    String carModel;

    @Schema(description = "Размеры шин")
    String dimensionOfTires;

    @Schema(description = "Услуги заказа")
    Set<OrderResponseDto> orders;
}
