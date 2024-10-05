package ru.antush59.attestation03.controller.dto.request;

import lombok.Value;

import java.util.List;

@Value
public class OrderRequestDto {
    String customerLogin;
    List<String> options;
}
