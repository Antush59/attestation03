package ru.antush59.attestation03.service;

import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.service.model.Option;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OptionsServiceTest {

    private OptionsRepository optionsRepository = Mockito.mock(OptionsRepository.class);
    private MapperFacade mapper = Mockito.mock(MapperFacade.class);
    private OptionsService optionsService = new OptionsService(optionsRepository, mapper);

    @Test
    void findAll() {
        //GIVEN
        OptionEntity optionEntity = new OptionEntity();
        optionEntity.setId(1L);
        optionEntity.setName("option1");
        optionEntity.setPrice(new BigDecimal("2500.00"));
        optionEntity.setOrders(Collections.emptyList());

        Option option = new Option();
        option.setId(1L);
        option.setName("option1");
        option.setPrice(new BigDecimal("2500.00"));

        Mockito.when(optionsRepository.findAllByDeletedIsFalse()).thenReturn(List.of(optionEntity));
        Mockito.when(mapper.map(optionEntity, Option.class)).thenReturn(option);


        //WHEN
        List<Option> options = optionsService.findAll();

        //THEN
        assertNotNull(options);
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));

        Mockito.verify(optionsRepository).findAllByDeletedIsFalse();
        Mockito.verify(mapper).map(optionEntity, Option.class);
        Mockito.verifyNoMoreInteractions(optionsRepository, mapper);
    }

    @Test
    void save() {
        //GIVEN
        OptionEntity optionEntity = new OptionEntity();
        optionEntity.setId(1L);
        optionEntity.setName("option1");
        optionEntity.setPrice(new BigDecimal("2500.00"));
        optionEntity.setOrders(Collections.emptyList());

        Option option = new Option();
        option.setId(1L);
        option.setName("option1");
        option.setPrice(new BigDecimal("2500.00"));

        Mockito.when(mapper.map(option, OptionEntity.class)).thenReturn(optionEntity);
        Mockito.when(optionsRepository.save(optionEntity)).thenReturn(optionEntity);
        Mockito.when(mapper.map(optionEntity, Option.class)).thenReturn(option);

        //WHEN
        Option saved = optionsService.save(option);

        //THEN
        assertNotNull(saved);
        assertEquals(saved, option);

        Mockito.verify(optionsRepository).save(optionEntity);
        Mockito.verify(mapper).map(option, OptionEntity.class);
        Mockito.verify(mapper).map(optionEntity, Option.class);
        Mockito.verifyNoMoreInteractions(optionsRepository, mapper);
    }

    @Test
    void deleteByName() {
        //GIVEN

        //WHEN
        optionsService.deleteByName("option1");
        //THEN
        Mockito.verify(optionsRepository).deleteByName("option1");
        Mockito.verifyNoMoreInteractions(optionsRepository, mapper);
    }

    @Test
    void findByName() {
        //GIVEN
        Option option = new Option();
        option.setId(1L);
        option.setName("option1");
        option.setPrice(new BigDecimal("2500.00"));

        OptionEntity entity = new OptionEntity();
        entity.setId(1L);
        entity.setName("option1");
        entity.setPrice(new BigDecimal("2500.00"));
        entity.setOrders(Collections.emptyList());

        Mockito.when(optionsRepository.findByNameAndDeletedFalse("option1")).thenReturn(Optional.of(entity));
        Mockito.when(mapper.map(entity, Option.class)).thenReturn(option);

        //WHEN
        Option byName = optionsService.findByName("option1");

        //THEN
        assertNotNull(option);
        assertEquals(byName, option);

        Mockito.verify(optionsRepository).findByNameAndDeletedFalse("option1");
        Mockito.verify(mapper).map(entity, Option.class);
        Mockito.verifyNoMoreInteractions(optionsRepository, mapper);
    }
}