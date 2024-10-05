package ru.antush59.attestation03.service;

import lombok.RequiredArgsConstructor;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.service.model.Option;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OptionsService {

    private final OptionsRepository optionsRepository;

    public List<Option> findAll() {
        Iterable<OptionEntity> optionEntities = optionsRepository.findAll();
        return StreamSupport.stream(optionEntities.spliterator(), false)
                .map(entity -> {
                    Option option = new Option();
                    option.setId(entity.getId());
                    option.setName(entity.getName());
                    option.setPrice(entity.getPrice());
                    return option;
                })
                .collect(Collectors.toList());
    }

    public Option save(Option option) {
        OptionEntity saved = optionsRepository.save(mapToEntity(option));
        option.setId(saved.getId());
        return option;
    }

    private OptionEntity mapToEntity(Option option) {
        OptionEntity optionEntity = new OptionEntity();
        optionEntity.setName(option.getName());
        optionEntity.setPrice(option.getPrice());

        return optionEntity;
    }
}

