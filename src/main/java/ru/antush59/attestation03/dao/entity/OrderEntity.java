package ru.antush59.attestation03.dao.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE tires.orders SET deleted = true WHERE id=?")
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE})
    @JoinColumn(name = "customer_login")
    @EqualsAndHashCode.Exclude
    private CustomerEntity customer;
    @CreationTimestamp
    private OffsetDateTime creationTime;

    @ManyToMany(mappedBy = "orders",
            cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<OptionEntity> options = new ArrayList<>();
    private boolean deleted;
}
