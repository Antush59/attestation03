package ru.antush59.attestation03.service.model;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
public class Customer {

    private String login;
    private String name;
    private String phoneNumber;
    private String carModel;
    private String dimensionOfTires;
    private List<Order> orders;
}
