package ru.antush59.attestation03.controller.dto.request;

import lombok.Value;

@Value
public class CustomerRequestDto {
    String login;
    String name;
    String phoneNumber;
    String carModel;
    String dimensionOfTires;
}
