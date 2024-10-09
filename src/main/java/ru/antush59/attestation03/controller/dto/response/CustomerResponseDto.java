package ru.antush59.attestation03.controller.dto.response;

import lombok.Value;

import java.util.Set;

@Value
public class CustomerResponseDto {
    String login;
    String name;
    String phoneNumber;
    String carModel;
    String dimensionOfTires;
    Set<OrderResponseDto> orders;
}
