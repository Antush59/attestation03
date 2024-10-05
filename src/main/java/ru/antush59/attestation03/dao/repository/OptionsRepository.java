package ru.antush59.attestation03.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.antush59.attestation03.dao.entity.OptionEntity;

import java.util.Optional;

public interface OptionsRepository extends CrudRepository<OptionEntity, Long> {

    Optional<OptionEntity> findByName(String name);
}
