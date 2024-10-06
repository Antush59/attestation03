package ru.antush59.attestation03.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antush59.attestation03.dao.entity.CustomerEntity;

import java.util.List;
import java.util.Optional;

public interface CustomersRepository extends CrudRepository<CustomerEntity, String> {
    List<CustomerEntity> findAllByDeletedIsFalse();

    void deleteByLogin(String login);

    Optional<CustomerEntity> findByLoginAndDeletedFalse(String login);
}
