package ru.antush59.attestation03.controller;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;
import ru.antush59.attestation03.controller.dto.request.CustomerRequestDto;
import ru.antush59.attestation03.controller.dto.response.CustomerResponseDto;
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
    public List<CustomerResponseDto> getCustomers() {
        return customersService.findAll().stream()
                .map(model -> mapper.map(model, CustomerResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public CustomerResponseDto saveCustomer(@RequestBody CustomerRequestDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        Customer saved = customersService.save(customer);
        return mapper.map(saved, CustomerResponseDto.class);
    }
}
