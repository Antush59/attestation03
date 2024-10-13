package ru.antush59.attestation03.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Клиенты", description = "Контроллер CRUD операций с клиентами")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomersService customersService;
    private final MapperFacade mapper;

    @Operation(summary = "Получить данные всех клиентов или клиента по указанному логину")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "500", description = "Задача не выполнена", content = @Content)
    })
    public List<CustomerResponseDto> getCustomers(@RequestParam(required = false) String login) {
        if (login == null) {
            return geAllCustomers();
        } else {
            return getCustomersByLogin(login);
        }
    }

    @Operation(summary = "Сохранить данные нового клиента")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные сохранены"),
            @ApiResponse(responseCode = "500", description = "Данные не сохранены", content = @Content)
    })
    public CustomerResponseDto saveCustomer(@RequestBody CustomerRequestDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        Customer saved = customersService.save(customer);
        return mapper.map(saved, CustomerResponseDto.class);
    }

    @Operation(summary = "Удалить данные клиента по указанному логину")
    @DeleteMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены"),
            @ApiResponse(responseCode = "500", description = "Данные не удалены", content = @Content)
    })
    public ResponseEntity<Void> deleteByLogin(@RequestParam String login) {
        customersService.deleteByLogin(login);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Изменить данные клиента")
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные изменены"),
            @ApiResponse(responseCode = "500", description = "Данные не изменены", content = @Content)
    })
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
