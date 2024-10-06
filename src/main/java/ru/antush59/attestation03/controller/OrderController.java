package ru.antush59.attestation03.controller;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.antush59.attestation03.controller.dto.request.OrderRequestDto;
import ru.antush59.attestation03.controller.dto.response.OrderResponseDto;
import ru.antush59.attestation03.controller.dto.update.OrderUpdateRequestDto;
import ru.antush59.attestation03.service.OrdersService;
import ru.antush59.attestation03.service.model.Order;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;
    private final MapperFacade mapper;


    @GetMapping
    public List<OrderResponseDto> getOrders(@RequestParam(required = false) Long id) {
        if (id == null) {
            return getAll();
        } else {
            return getById(id);
        }
    }

    @PostMapping
    public OrderResponseDto saveOrder(@RequestBody OrderRequestDto order) {
        Order saved = ordersService.save(mapper.map(order, Order.class));
        return mapper.map(saved, OrderResponseDto.class);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestParam Long id) {
        ordersService.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping
    public OrderResponseDto updateOrder(@RequestBody OrderUpdateRequestDto orderUpdateDto) {
        Order saved = ordersService.save(mapper.map(orderUpdateDto, Order.class));
        return mapper.map(saved, OrderResponseDto.class);
    }

    private @NotNull List<OrderResponseDto> getAll() {
        return ordersService.findAll().stream()
                .map(model -> mapper.map(model, OrderResponseDto.class))
                .collect(Collectors.toList());
    }

    private @NotNull List<OrderResponseDto> getById(Long id) {
        Order byId = ordersService.findById(id);
        return List.of(mapper.map(byId, OrderResponseDto.class));
    }
}
