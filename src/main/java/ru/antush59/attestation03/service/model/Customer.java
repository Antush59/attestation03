package ru.antush59.attestation03.service.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Customer {

    private String login;
    private String name;
    private String phoneNumber;
    private String carModel;
    private String dimensionOfTires;
    private List<Order> orders;
}
