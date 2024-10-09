package ru.antush59.attestation03.controller.dto.update;

import lombok.Value;

import java.util.Set;

@Value
public class CustomerUpdateRequestDto {
    String login;
    String name;
    String phoneNumber;
    String carModel;
    String dimensionOfTires;
    Set<OrderUpdateRequestDto> orders;
}
