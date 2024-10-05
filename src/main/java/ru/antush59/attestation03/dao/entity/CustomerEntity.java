package ru.antush59.attestation03.dao.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class CustomerEntity {

    @Id
    private String login;
    private String name;
    private String phoneNumber;
    private String carModel;
    private String dimensionOfTires;
    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderEntity> orders = new HashSet<>();
}
