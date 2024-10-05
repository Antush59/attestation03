package ru.antush59.attestation03.controller;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;
import ru.antush59.attestation03.controller.dto.request.OrderRequestDto;
import ru.antush59.attestation03.controller.dto.response.OrderResponseDto;
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
    public List<OrderResponseDto> getOrders() {
        return ordersService.findAll().stream().map(model -> mapper.map(model, OrderResponseDto.class)).collect(Collectors.toList());
    }

    @PostMapping
    public OrderResponseDto saveOrder(@RequestBody OrderRequestDto order) {
        Order saved = ordersService.save(mapper.map(order, Order.class));
        return mapper.map(saved, OrderResponseDto.class);
    }
}
