package ru.antush59.attestation03.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.entity.OrderEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.dao.repository.OrdersRepository;
import ru.antush59.attestation03.service.model.Option;
import ru.antush59.attestation03.service.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OptionsRepository optionsRepository;
    private final CustomersRepository customersRepository;
    private final MapperFacade mapper;

    public List<Order> findAll() {
        Iterable<OrderEntity> orderEntities = ordersRepository.findAll();
        return StreamSupport.stream(orderEntities.spliterator(), false)
                .map(entity -> mapper.map(entity, Order.class))
                .collect(Collectors.toList());
    }

    public Order save(Order order) {
        List<OptionEntity> optionEntities = collectOptionsFromDb(order);
        OrderEntity orderEntity = mapper.map(order, OrderEntity.class);
        orderEntity.setOptions(optionEntities);

        CustomerEntity customerEntity = customersRepository.findById(order.getCustomerLogin())
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден!"));
        orderEntity.setCustomer(customerEntity);
        OrderEntity saved = ordersRepository.save(orderEntity);
        optionEntities = optionEntities.stream()
                .peek(optionEntity -> {
                    List<OrderEntity> orders = optionEntity.getOrders();
                    orders.add(saved);
                })
                .collect(Collectors.toList());
        optionsRepository.saveAll(optionEntities);
        customerEntity.getOrders().add(orderEntity);
        customersRepository.save(customerEntity);
        return mapper.map(saved, Order.class);
    }

    private @NotNull List<OptionEntity> collectOptionsFromDb(Order order) {
        return order.getOptions().stream()
                .map(Option::getName)
                .map(optionsRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
