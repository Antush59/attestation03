package ru.antush59.attestation03.dao.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "options")
@SQLDelete(sql = "UPDATE tires.options SET deleted = true WHERE id=?")
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE})
    @JoinTable(
            name = "order_option",
            joinColumns = {@JoinColumn(name = "option_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_id")}
    )
    @ToString.Exclude
    private List<OrderEntity> orders = new ArrayList<>();
    private boolean deleted;
}
