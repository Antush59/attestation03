package ru.antush59.attestation03.service;

import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;
import ru.antush59.attestation03.service.model.Customer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomersServiceTest {

    private CustomersRepository customerRepository = Mockito.mock(CustomersRepository.class);
    private MapperFacade mapper = Mockito.mock(MapperFacade.class);
    private CustomersService customersService = new CustomersService(customerRepository, mapper);

    @Test
    void findAll() {
        //GIVEN
        CustomerEntity entity = new CustomerEntity();
        entity.setName("sky");
        entity.setLogin("sky");
        entity.setPhoneNumber("123");
        entity.setCarModel("auto");
        entity.setDimensionOfTires("11");
        entity.setOrders(Collections.emptySet());

        Customer customer = new Customer();
        customer.setName("sky");
        customer.setLogin("sky");
        customer.setPhoneNumber("123");
        customer.setCarModel("auto");
        customer.setDimensionOfTires("11");
        customer.setOrders(Collections.emptySet());

        Mockito.when(customerRepository.findAllByDeletedIsFalse()).thenReturn(List.of(entity));
        Mockito.when(mapper.map(entity, Customer.class)).thenReturn(customer);

        //WHEN
        List<Customer> list = customersService.findAll();

        //THEN
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(customer, list.get(0));

        Mockito.verify(customerRepository).findAllByDeletedIsFalse();
        Mockito.verify(mapper).map(entity, Customer.class);
        Mockito.verifyNoMoreInteractions(customerRepository, mapper);
    }

    @Test
    void save() {
        //GIVEN
        Customer customer = new Customer();
        customer.setLogin("antush");
        customer.setName("anton");
        customer.setPhoneNumber("429752398");
        customer.setCarModel("polo");
        customer.setDimensionOfTires("2421/123/11");
        customer.setOrders(Collections.emptySet());

        CustomerEntity entity = new CustomerEntity();
        entity.setLogin("antush");
        entity.setName("anton");
        entity.setPhoneNumber("429752398");
        entity.setCarModel("polo");
        entity.setDimensionOfTires("2421/123/11");
        entity.setOrders(Collections.emptySet());

        Mockito.when(mapper.map(customer, CustomerEntity.class)).thenReturn(entity);
        Mockito.when(customerRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.map(entity, Customer.class)).thenReturn(customer);

        //WHEN
        Customer saved = customersService.save(customer);

        //THEN
        assertNotNull(saved);
        assertEquals(customer, saved);

        Mockito.verify(customerRepository).save(entity);
        Mockito.verify(mapper).map(customer, CustomerEntity.class);
        Mockito.verify(mapper).map(entity, Customer.class);
        Mockito.verifyNoMoreInteractions(customerRepository, mapper);
    }

    @Test
    void deleteByLogin() {
        //GIVEN

        //WHEN
        customersService.deleteByLogin("antush");
        //THEN
        Mockito.verify(customerRepository).deleteByLogin("antush");
        Mockito.verifyNoMoreInteractions(customerRepository, mapper);
    }

    @Test
    void findByLogin() {
        //GIVEN
        Customer customer = new Customer();
        customer.setLogin("antush");
        customer.setName("anton");
        customer.setPhoneNumber("429752398");
        customer.setCarModel("polo");
        customer.setDimensionOfTires("2421/123/11");
        customer.setOrders(Collections.emptySet());

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setLogin("antush");
        customerEntity.setName("anton");
        customerEntity.setPhoneNumber("429752398");
        customerEntity.setCarModel("polo");
        customerEntity.setDimensionOfTires("2421/123/11");
        customerEntity.setOrders(Collections.emptySet());

        Mockito.when(customerRepository.findByLoginAndDeletedFalse("antush")).thenReturn(Optional.of(customerEntity));
        Mockito.when(mapper.map(customerEntity, Customer.class)).thenReturn(customer);
        //WHEN
        Customer byLogin = customersService.findByLogin("antush");
        //THEN
        assertNotNull(customer);
        assertEquals(byLogin, customer);

        Mockito.verify(customerRepository).findByLoginAndDeletedFalse("antush");
        Mockito.verify(mapper).map(customerEntity, Customer.class);
        Mockito.verifyNoMoreInteractions(customerRepository, mapper);
    }
}