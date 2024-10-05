package ru.antush59.attestation03.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;
import ru.antush59.attestation03.service.model.Customer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CustomersService {

    private final CustomersRepository customerRepository;

    public List<Customer> findAll() {
        Iterable<CustomerEntity> costumerEntities = customerRepository.findAll();
        return StreamSupport.stream(costumerEntities.spliterator(), false)
                .map(entity -> new Customer(entity.getLogin(),
                        entity.getName(),
                        entity.getPhoneNumber(),
                        entity.getCarModel(),
                        entity.getDimensionOfTires()))
                .collect(Collectors.toList());
    }

    public Customer findByLogin(String customerLogin) {
        return customerRepository.findById(customerLogin)
                .map(entity -> new Customer(entity.getLogin(), entity.getName(),
                        entity.getPhoneNumber(),
                        entity.getCarModel(), entity.getDimensionOfTires()))
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден!"));
    }

    public Customer save(Customer customer) {
        CustomerEntity saved = customerRepository.save(mapToEntity(customer));
        return customer;
    }

    private CustomerEntity mapToEntity(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setLogin(customer.getLogin());
        customerEntity.setName(customer.getName());
        customerEntity.setPhoneNumber(customer.getPhoneNumber());
        customerEntity.setCarModel(customer.getCarModel());
        customerEntity.setDimensionOfTires(customer.getDimensionOfTires());
        return customerEntity;
    }
}
