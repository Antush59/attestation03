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
import ru.antush59.attestation03.controller.dto.request.OrderRequestDto;
import ru.antush59.attestation03.controller.dto.response.OrderResponseDto;
import ru.antush59.attestation03.controller.dto.update.OrderUpdateRequestDto;
import ru.antush59.attestation03.service.OrdersService;
import ru.antush59.attestation03.service.model.Order;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Заказы", description = "Контроллер для CRUD операций для работы с заказами")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;
    private final MapperFacade mapper;

    @Operation(summary = "Получить данные всех заказов или заказа по указанному id")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "500", description = "Задача не выполнена", content = @Content)
    })
    public List<OrderResponseDto> getOrders(@RequestParam(required = false) Long id) {
        if (id == null) {
            return getAll();
        } else {
            return getById(id);
        }
    }

    @Operation(summary = "Сохранить данные нового заказа")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные сохранены"),
            @ApiResponse(responseCode = "500", description = "Данные не сохранены", content = @Content)
    })
    public OrderResponseDto saveOrder(@RequestBody OrderRequestDto order) {
        Order saved = ordersService.save(mapper.map(order, Order.class));
        return mapper.map(saved, OrderResponseDto.class);
    }

    @Operation(summary = "Удалить данные заказа по указанному id")
    @DeleteMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены"),
            @ApiResponse(responseCode = "500", description = "Данные не удалены", content = @Content)
    })
    public ResponseEntity<Void> deleteById(@RequestParam Long id) {
        ordersService.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Изменить данные заказа")
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные изменены"),
            @ApiResponse(responseCode = "500", description = "Данные не изменены", content = @Content)
    })
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
