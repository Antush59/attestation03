package ru.antush59.attestation03.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequestDto {
    private String customerLogin;
    private List<String> options;
}
