package ru.antush59.attestation03.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.entity.OrderEntity;
import ru.antush59.attestation03.dao.repository.OptionsRepository;
import ru.antush59.attestation03.dao.repository.OrdersRepository;
import ru.antush59.attestation03.service.model.Customer;
import ru.antush59.attestation03.service.model.Option;
import ru.antush59.attestation03.service.model.Order;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OptionsRepository optionsRepository;

    public List<Order> findAll() {
        Iterable<OrderEntity> orderEntities = ordersRepository.findAll();
        return StreamSupport.stream(orderEntities.spliterator(), false)
                .map(entity -> new Order(entity.getId(),
                        mapCustomer(entity.getCustomer()),
                        entity.getCreationTime(),
                        mapOptions(entity.getOptions())))
                .collect(Collectors.toList());
    }

    public Order save(Order order) {
        List<OptionEntity> optionEntities = mapToOptionEntities(order.getOptions());
        OrderEntity saved = ordersRepository.save(mapToEntity(order, optionEntities));
        optionEntities = optionEntities.stream()
                .map(optionEntity -> {
                    List<OrderEntity> orders = optionEntity.getOrders();
                    orders.add(saved);
                    return optionEntity;
                })
                .collect(Collectors.toList());
        optionsRepository.saveAll(optionEntities);
        return order;
    }

    private List<Option> mapOptions(List<OptionEntity> options) {
        return options.stream()
                .map(entity -> new Option(entity.getId(), entity.getName(),
                        entity.getPrice()))
                .collect(Collectors.toList());
    }

    private Customer mapCustomer(CustomerEntity customerEntity) {
        return new Customer(customerEntity.getLogin(),
                customerEntity.getName(),
                customerEntity.getPhoneNumber(),
                customerEntity.getCarModel(),
                customerEntity.getDimensionOfTires());
    }

    private OrderEntity mapToEntity(Order order, List<OptionEntity> optionEntities) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOptions(optionEntities);
        Customer customer = order.getCustomer();
        orderEntity.setCustomer(createCustomer(customer));
        orderEntity.setCreationTime(order.getCreationTime());
        return orderEntity;
    }

    private static CustomerEntity createCustomer(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setLogin(customer.getLogin());
        customerEntity.setName(customer.getName());
        customerEntity.setPhoneNumber(customer.getPhoneNumber());
        customerEntity.setCarModel(customer.getCarModel());
        customerEntity.setDimensionOfTires(customer.getDimensionOfTires());
        return customerEntity;
    }

    private List<OptionEntity> mapToOptionEntities(List<Option> options) {
        return options.stream()
                .map(option -> {
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setId(option.getId());
                    optionEntity.setName(option.getName());
                    optionEntity.setPrice(option.getPrice());
                    return optionEntity;
                })
                .collect(Collectors.toList());
    }
}
