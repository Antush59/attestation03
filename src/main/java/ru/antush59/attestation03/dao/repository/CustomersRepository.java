package ru.antush59.attestation03.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antush59.attestation03.dao.entity.CustomerEntity;

public interface CustomersRepository extends CrudRepository<CustomerEntity, String> {
}
