package ru.antush59.attestation03.controller;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.antush59.attestation03.controller.dto.request.CustomerRequestDto;
import ru.antush59.attestation03.controller.dto.response.CustomerResponseDto;
import ru.antush59.attestation03.controller.dto.update.CustomerUpdateRequestDto;
import ru.antush59.attestation03.service.CustomersService;
import ru.antush59.attestation03.service.model.Customer;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomersService customersService;
    private final MapperFacade mapper;

    @GetMapping
    public List<CustomerResponseDto> getCustomers(@RequestParam(required = false) String login) {
        if (login == null) {
            return geAllCustomers();
        } else {
            return getCustomersByLogin(login);
        }
    }

    @PostMapping
    public CustomerResponseDto saveCustomer(@RequestBody CustomerRequestDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        Customer saved = customersService.save(customer);
        return mapper.map(saved, CustomerResponseDto.class);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByLogin(@RequestParam String login) {
        customersService.deleteByLogin(login);
        return ResponseEntity.status(204).build();
    }

    @PutMapping
    public CustomerResponseDto updateCustomer(@RequestBody CustomerUpdateRequestDto customerUpdateDto) {
        Customer customer = mapper.map(customerUpdateDto, Customer.class);
        Customer saved = customersService.save(customer);
        return mapper.map(saved, CustomerResponseDto.class);
    }

    private @NotNull List<CustomerResponseDto> getCustomersByLogin(String login) {
        Customer byLogin = customersService.findByLogin(login);
        return List.of(mapper.map(byLogin, CustomerResponseDto.class));
    }

    private @NotNull List<CustomerResponseDto> geAllCustomers() {
        return customersService.findAll().stream()
                .map(model -> mapper.map(model, CustomerResponseDto.class))
                .collect(Collectors.toList());
    }
}
