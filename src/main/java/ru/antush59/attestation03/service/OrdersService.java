package ru.antush59.attestation03.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.entity.OrderEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.dao.repository.OrdersRepository;
import ru.antush59.attestation03.service.model.Option;
import ru.antush59.attestation03.service.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OptionsRepository optionsRepository;
    private final CustomersRepository customersRepository;
    private final MapperFacade mapper;

    public List<Order> findAll() {
        List<OrderEntity> orderEntities = ordersRepository.findAllByDeletedIsFalse();
        return orderEntities.stream()
                .map(entity -> mapper.map(entity, Order.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order save(Order order) {
        List<OptionEntity> optionEntities = collectOptionsFromDb(order);
        OrderEntity orderEntity = mapper.map(order, OrderEntity.class);
        orderEntity.setOptions(optionEntities);

        CustomerEntity customerEntity = customersRepository.findById(order.getCustomerLogin())
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден!"));
        orderEntity.setCustomer(customerEntity);
        OrderEntity saved = ordersRepository.save(orderEntity);
        List<OptionEntity> toSave = new ArrayList<>();
        optionEntities
                .forEach(optionEntity -> {
                    List<OrderEntity> orders = optionEntity.getOrders();
                    if (!orders.contains(saved)) {
                        orders.add(saved);
                    }
                    toSave.add(optionEntity);
                });

        optionsRepository.saveAll(toSave);

        Set<OrderEntity> customerEntityOrders = customerEntity.getOrders();
        if (!customerEntityOrders.contains(saved)) {
            customerEntityOrders.add(saved);
            customersRepository.save(customerEntity);
        }
        return mapper.map(saved, Order.class);
    }

    private @NotNull List<OptionEntity> collectOptionsFromDb(Order order) {
        return order.getOptions().stream()
                .map(Option::getName)
                .map(optionsRepository::findByNameAndDeletedFalse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        ordersRepository.deleteById(id);
    }

    public Order findById(Long id) {
        Optional<OrderEntity> byId = ordersRepository.findByIdAndDeletedFalse(id);
        OrderEntity orderEntity = byId.orElseThrow(() -> new EntityNotFoundException("Заказ не был найден!"));
        return mapper.map(orderEntity, Order.class);
    }
}
