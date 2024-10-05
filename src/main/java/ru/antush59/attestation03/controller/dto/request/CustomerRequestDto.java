package ru.antush59.attestation03.controller.dto.request;

import lombok.Data;

@Data
public class CustomerRequestDto {
    private String login;
    private String name;
    private String phoneNumber;
    private String carModel;
    private String dimensionOfTires;
}
