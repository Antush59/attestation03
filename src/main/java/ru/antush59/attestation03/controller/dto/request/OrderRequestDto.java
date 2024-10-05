package ru.antush59.attestation03.controller.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private String customerLogin;
    private List<String> options;
}
