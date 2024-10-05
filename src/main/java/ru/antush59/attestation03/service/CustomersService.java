package ru.antush59.attestation03.service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
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
    private final MapperFacade mapper;

    public List<Customer> findAll() {
        Iterable<CustomerEntity> costumerEntities = customerRepository.findAll();
        return StreamSupport.stream(costumerEntities.spliterator(), false)
                .map(entity -> mapper.map(entity, Customer.class))
                .collect(Collectors.toList());
    }

    public Customer save(Customer customer) {
        CustomerEntity saved = customerRepository.save(mapper.map(customer, CustomerEntity.class));
        return mapper.map(saved, Customer.class);
    }
}
