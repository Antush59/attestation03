package ru.antush59.attestation03.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.transaction.annotation.Transactional;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.service.model.Option;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OptionsService {

    private final OptionsRepository optionsRepository;
    private final MapperFacade mapper;

    public List<Option> findAll() {
        List<OptionEntity> optionEntities = optionsRepository.findAllByDeletedIsFalse();
        return optionEntities.stream()
                .map(entity -> mapper.map(entity, Option.class))
                .collect(Collectors.toList());
    }

    public Option save(Option option) {
        OptionEntity saved = optionsRepository.save(mapper.map(option, OptionEntity.class));
        return mapper.map(saved, Option.class);
    }

    @Transactional
    public void deleteByName(String name) {
        optionsRepository.deleteByName(name);
    }

    public Option findByName(String name) {
        Optional<OptionEntity> byName = optionsRepository.findByNameAndDeletedFalse(name);
        OptionEntity optionEntity = byName.orElseThrow(() -> new EntityNotFoundException("Услуга не найдена!"));
        return mapper.map(optionEntity, Option.class);
    }
}

