package ru.antush59.attestation03.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antush59.attestation03.dao.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByDeletedIsFalse();

    void deleteById(Long id);

    Optional<OrderEntity> findByIdAndDeletedFalse(Long id);
}
