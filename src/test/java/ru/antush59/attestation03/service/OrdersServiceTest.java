package ru.antush59.attestation03.service;

import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.entity.OrderEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.dao.repository.OrdersRepository;
import ru.antush59.attestation03.service.model.Order;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

    private OrdersRepository ordersRepository = Mockito.mock(OrdersRepository.class);
    private OptionsRepository optionsRepository = Mockito.mock(OptionsRepository.class);
    private CustomersRepository customersRepository = Mockito.mock(CustomersRepository.class);
    private MapperFacade mapper = Mockito.mock(MapperFacade.class);
    private OrdersService ordersService = new OrdersService(ordersRepository, optionsRepository,
            customersRepository, mapper);

    @Test
    void findAll() {
        //GIVE
        CustomerEntity customer = new CustomerEntity();
        customer.setName("sky");
        customer.setLogin("sky");
        customer.setPhoneNumber("123");
        customer.setCarModel("auto");
        customer.setDimensionOfTires("11");
        customer.setOrders(Collections.emptySet());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCustomer(customer);
        orderEntity.setCreationTime(OffsetDateTime.now());
        orderEntity.setOptions(Collections.emptyList());

        Order order = new Order();
        order.setId(1L);
        order.setCustomerLogin(customer.getLogin());
        order.setCreationTime(OffsetDateTime.now());
        order.setOptions(Collections.emptyList());

        Mockito.when(ordersRepository.findAllByDeletedIsFalse()).thenReturn(List.of(orderEntity));
        Mockito.when(mapper.map(orderEntity, Order.class)).thenReturn(order);

        //WHEN
        List<Order> allOrders = ordersService.findAll();

        //THEN
        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
        assertEquals(order, allOrders.get(0));

        Mockito.verify(ordersRepository).findAllByDeletedIsFalse();
        Mockito.verify(mapper).map(orderEntity, Order.class);
        Mockito.verifyNoMoreInteractions(ordersRepository, mapper);
    }

    @Test
    void save() {
        //GIVE
        CustomerEntity customer = new CustomerEntity();
        customer.setName("sky");
        customer.setLogin("sky");
        customer.setPhoneNumber("123");
        customer.setCarModel("auto");
        customer.setDimensionOfTires("11");
        customer.setOrders(new HashSet<>());

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("sky");
        customerEntity.setLogin("sky");
        customerEntity.setPhoneNumber("123");
        customerEntity.setCarModel("auto");
        customerEntity.setDimensionOfTires("11");
        customerEntity.setOrders(new HashSet<>());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCustomer(customer);
        orderEntity.setCreationTime(OffsetDateTime.now());
        orderEntity.setOptions(Collections.emptyList());

        Order order = new Order();
        order.setId(1L);
        order.setCustomerLogin(customer.getLogin());
        order.setCreationTime(OffsetDateTime.now());
        order.setOptions(Collections.emptyList());

        Mockito.when(mapper.map(order, OrderEntity.class)).thenReturn(orderEntity);
        Mockito.when(customersRepository.findById(order.getCustomerLogin())).thenReturn(Optional.of(customerEntity));
        Mockito.when(mapper.map(orderEntity, Order.class)).thenReturn(order);
        Mockito.when(ordersRepository.save(orderEntity)).thenReturn(orderEntity);


        //WHEN
        Order saved = ordersService.save(order);

        //THEN
        assertNotNull(saved);
        assertEquals(order, saved);

        Mockito.verify(mapper).map(order, OrderEntity.class);
        Mockito.verify(mapper).map(orderEntity, Order.class);
        Mockito.verify(customersRepository).findById(order.getCustomerLogin());
        Mockito.verify(ordersRepository).save(orderEntity);
        Mockito.verifyNoMoreInteractions(ordersRepository, mapper);
    }

    @Test
    void deleteById() {
        //GIVEN

        //WHEN
        ordersService.deleteById(1L);
        //THEN
        Mockito.verify(ordersRepository).deleteById(1L);
        Mockito.verifyNoMoreInteractions(ordersRepository, mapper);
    }

    @Test
    void findById() {
        //GIVEN
        CustomerEntity customer = new CustomerEntity();
        customer.setName("sky");
        customer.setLogin("sky");
        customer.setPhoneNumber("123");
        customer.setCarModel("auto");
        customer.setDimensionOfTires("11");
        customer.setOrders(Collections.emptySet());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCustomer(customer);
        orderEntity.setCreationTime(OffsetDateTime.now());
        orderEntity.setOptions(Collections.emptyList());

        Order order = new Order();
        order.setId(1L);
        order.setCustomerLogin(customer.getLogin());
        order.setCreationTime(OffsetDateTime.now());
        order.setOptions(Collections.emptyList());

        Mockito.when(ordersRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(orderEntity));
        Mockito.when(mapper.map(orderEntity, Order.class)).thenReturn(order);
        //WHEN
        Order byId = ordersService.findById(1L);
        //THEN
        assertNotNull(order);
        assertEquals(byId, order);

        Mockito.verify(ordersRepository).findByIdAndDeletedFalse(1L);
        Mockito.verify(mapper).map(orderEntity, Order.class);
        Mockito.verifyNoMoreInteractions(ordersRepository, mapper);
    }
}