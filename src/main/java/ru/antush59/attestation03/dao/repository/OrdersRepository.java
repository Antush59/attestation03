package ru.antush59.attestation03.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antush59.attestation03.dao.entity.OrderEntity;

public interface OrdersRepository extends CrudRepository<OrderEntity, Long> {
}
