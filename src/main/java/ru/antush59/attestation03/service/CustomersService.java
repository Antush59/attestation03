package ru.antush59.attestation03.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;
import ru.antush59.attestation03.service.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomersService {

    private final CustomersRepository customerRepository;
    private final MapperFacade mapper;

    public List<Customer> findAll() {
        List<CustomerEntity> costumerEntities = customerRepository.findAllByDeletedIsFalse();
        return costumerEntities.stream()
                .map(entity -> mapper.map(entity, Customer.class))
                .collect(Collectors.toList());
    }

    public Customer save(Customer customer) {
        CustomerEntity saved = customerRepository.save(mapper.map(customer, CustomerEntity.class));
        return mapper.map(saved, Customer.class);
    }

    @Transactional
    public void deleteByLogin(String login) {
        customerRepository.deleteByLogin(login);
    }

    public Customer findByLogin(String login) {
        Optional<CustomerEntity> customerByLogin = customerRepository.findByLoginAndDeletedFalse(login);
        CustomerEntity customerEntity = customerByLogin.orElseThrow(() ->
                new EntityNotFoundException("Клиент не найден!"));
        return mapper.map(customerEntity, Customer.class);
    }
}
