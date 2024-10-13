package ru.antush59.attestation03.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Пользователь")
public class CustomerRequestDto {

    @Schema(description = "Уникальный логин")
    private String login;

    @Schema(description = "Имя пользователя")
    private String name;

    @Schema(description = "Номер телефона")
    private String phoneNumber;

    @Schema(description = "Модель автомобиля")
    private String carModel;

    @Schema(description = "Размеры шин")
    private String dimensionOfTires;
}
