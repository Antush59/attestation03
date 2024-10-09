package ru.antush59.attestation03.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import ru.antush59.attestation03.BaseIT;
import ru.antush59.attestation03.controller.dto.request.OrderRequestDto;
import ru.antush59.attestation03.controller.dto.response.OptionResponseDto;
import ru.antush59.attestation03.controller.dto.response.OrderResponseDto;
import ru.antush59.attestation03.controller.dto.update.OptionUpdateRequestDto;
import ru.antush59.attestation03.controller.dto.update.OrderUpdateRequestDto;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.entity.OrderEntity;
import ru.antush59.attestation03.dao.repository.OrdersRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerIT extends BaseIT {

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    @Sql(value = "/db/scripts/create_orders.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnOrders() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();

        String content = response.getContentAsString();

        assertNotNull(content);

        List<OrderResponseDto> orders = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(orders);

        assertEquals(1, orders.size());

        assertThat(orders.get(0))
                .isNotNull()
                .returns(1L, OrderResponseDto::getId)
                .returns("johny_bee", OrderResponseDto::getCustomerLogin)
                .returns(LocalDate.now(), order -> order.getCreationTime().toLocalDate())
                .extracting(OrderResponseDto::getOptions)
                .returns(1, List::size)
                .extracting(list -> list.get(0))
                .returns("option1", OptionResponseDto::getName)
                .returns(new BigDecimal("1234.20"), OptionResponseDto::getPrice);
    }

    @Test
    @Sql(value = "/db/scripts/create_orders.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnByIdOrder() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/orders")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();

        String content = response.getContentAsString();

        assertNotNull(content);

        List<OrderResponseDto> orders = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(orders);

        assertEquals(1, orders.size());

        assertThat(orders.get(0))
                .isNotNull()
                .returns(1L, OrderResponseDto::getId)
                .returns("johny_bee", OrderResponseDto::getCustomerLogin)
                .returns(LocalDate.now(), order -> order.getCreationTime().toLocalDate())
                .extracting(OrderResponseDto::getOptions)
                .returns(1, List::size)
                .extracting(list -> list.get(0))
                .returns("option1", OptionResponseDto::getName)
                .returns(new BigDecimal("1234.20"), OptionResponseDto::getPrice);
    }

    @Test
    @Sql(value = "/db/scripts/create_customer_and_option.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldSaveOrder() throws Exception {
        Optional<OrderEntity> orderEntity = ordersRepository.findByIdAndDeletedFalse(2L);
        assertTrue(orderEntity.isEmpty());

        OrderRequestDto orderRequestDto = new OrderRequestDto("johny_bee", List.of("option1"));
        String requestBody = toJson(orderRequestDto);
        mockMvc.perform(post("/orders")
                        .content(requestBody).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<OrderEntity> orderEntityOptional = ordersRepository.findByIdAndDeletedFalse(1L);
            assertThat(orderEntityOptional)
                    .isPresent()
                    .get()
                    .returns(1L, OrderEntity::getId)
                    .returns("johny_bee", order -> order.getCustomer().getLogin())
                    .returns(LocalDate.now(), order -> order.getCreationTime().toLocalDate())
                    .extracting(OrderEntity::getOptions)
                    .returns(1, List::size)
                    .extracting(list -> list.get(0))
                    .returns("option1", OptionEntity::getName)
                    .returns(new BigDecimal("1234.20"), OptionEntity::getPrice);
        });
    }

    @Test
    @Sql(value = "/db/scripts/create_orders.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteByIdOrder() throws Exception {
        Optional<OrderEntity> orderEntity = ordersRepository.findByIdAndDeletedFalse(1L);
        assertTrue(orderEntity.isPresent());

        mockMvc.perform(delete("/orders")
                        .param("id", "1"))
                .andExpect(status().isNoContent())
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<OrderEntity> orderEntityOptional = ordersRepository.findByIdAndDeletedFalse(1L);
            assertThat(orderEntityOptional)
                    .isEmpty();
        });
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_orders.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpdateOrder() throws Exception {
        Optional<OrderEntity> orderEntity = ordersRepository.findByIdAndDeletedFalse(1L);
        assertThat(orderEntity)
                .isPresent()
                .get()
                .returns(1L, OrderEntity::getId)
                .returns("johny_bee", order -> order.getCustomer().getLogin())
                .returns(LocalDate.now(), order -> order.getCreationTime().toLocalDate())
                .extracting(OrderEntity::getOptions)
                .returns(1, List::size)
                .extracting(list -> list.get(0))
                .returns("option1", OptionEntity::getName)
                .returns(new BigDecimal("1234.20"), OptionEntity::getPrice);

        OrderUpdateRequestDto orderUpdateRequestDto = new OrderUpdateRequestDto(1L, "antush",
                OffsetDateTime.now(), List.of(new OptionUpdateRequestDto(1L, "option2",
                new BigDecimal("580.50"))));

        String requestBody = toJson(orderUpdateRequestDto);
        mockMvc.perform(put("/orders")
                        .content(requestBody).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<OrderEntity> orderEntityOptional = ordersRepository.findByIdAndDeletedFalse(1L);
            assertThat(orderEntityOptional)
                    .isPresent()
                    .get()
                    .returns(1L, OrderEntity::getId)
                    .returns("antush", order -> order.getCustomer().getLogin())
                    .returns(LocalDate.now(), order -> order.getCreationTime().toLocalDate())
                    .extracting(OrderEntity::getOptions)
                    .returns(2, List::size);
        });
    }
}
