package ru.antush59.attestation03.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import ru.antush59.attestation03.BaseIT;
import ru.antush59.attestation03.controller.dto.request.CustomerRequestDto;
import ru.antush59.attestation03.controller.dto.response.CustomerResponseDto;
import ru.antush59.attestation03.controller.dto.update.CustomerUpdateRequestDto;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.repository.CustomersRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerIT extends BaseIT {
    @Autowired
    private CustomerController customerController;
    @Autowired
    private CustomersRepository customersRepository;


    @Test
    @Sql(value = "/db/scripts/create_customers.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnCustomers() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();

        String content = response.getContentAsString();

        assertNotNull(content);

        List<CustomerResponseDto> customers = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(customers);

        assertEquals(1, customers.size());

        assertThat(customers.get(0))
                .isNotNull()
                .returns("johny_bee", CustomerResponseDto::getLogin)
                .returns("John Lennon", CustomerResponseDto::getName)
                .returns("5553535", CustomerResponseDto::getPhoneNumber)
                .returns("Ford", CustomerResponseDto::getCarModel)
                .returns("180/65/r15", CustomerResponseDto::getDimensionOfTires)
                .returns(Collections.emptyList(), CustomerResponseDto::getOrders);
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_customers.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnByLoginCustomer() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/customers")
                        .param("login", "sky"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();

        String content = response.getContentAsString();

        assertNotNull(content);

        List<CustomerResponseDto> customers = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(customers);

        assertEquals(1, customers.size());

        assertThat(customers.get(0))
                .isNotNull()
                .returns("sky", CustomerResponseDto::getLogin)
                .returns("Luck Skywalker", CustomerResponseDto::getName)
                .returns("5212553535", CustomerResponseDto::getPhoneNumber)
                .returns("The Falcon of the Millennium", CustomerResponseDto::getCarModel)
                .returns("1200/615/r115", CustomerResponseDto::getDimensionOfTires)
                .returns(Collections.emptyList(), CustomerResponseDto::getOrders);
    }

    @Test
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldSaveCustomer() throws Exception {
        Optional<CustomerEntity> ant = customersRepository.findByLoginAndDeletedFalse("ant");
        assertTrue(ant.isEmpty());

        CustomerRequestDto customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setLogin("ant");
        customerRequestDto.setName("Anton");
        customerRequestDto.setPhoneNumber("37482-83475471");
        customerRequestDto.setCarModel("VWPolo");
        customerRequestDto.setDimensionOfTires("180/65/r15");
        String requestBody = toJson(customerRequestDto);
        mockMvc.perform(post("/customers")
                        .content(requestBody).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<CustomerEntity> antCustomer = customersRepository.findByLoginAndDeletedFalse("ant");
            assertThat(antCustomer)
                    .isPresent()
                    .get()
                    .returns("ant", CustomerEntity::getLogin)
                    .returns("Anton", CustomerEntity::getName)
                    .returns("37482-83475471", CustomerEntity::getPhoneNumber)
                    .returns("VWPolo", CustomerEntity::getCarModel)
                    .returns("180/65/r15", CustomerEntity::getDimensionOfTires)
                    .returns(Collections.emptySet(), CustomerEntity::getOrders);
        });
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_customers.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteCustomer() throws Exception {
        Optional<CustomerEntity> sky = customersRepository.findByLoginAndDeletedFalse("sky");
        assertTrue(sky.isPresent());

        mockMvc.perform(delete("/customers")
                        .param("login", "sky"))
                .andExpect(status().isNoContent())
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<CustomerEntity> skyCustomer = customersRepository.findByLoginAndDeletedFalse("sky");
            assertThat(skyCustomer)
                    .isEmpty();
        });
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_customers.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpdateCustomer() throws Exception {
        Optional<CustomerEntity> sky = customersRepository.findByLoginAndDeletedFalse("sky");
        assertThat(sky)
                .isPresent()
                .get()
                .returns("sky", CustomerEntity::getLogin)
                .returns("Luck Skywalker", CustomerEntity::getName)
                .returns("5212553535", CustomerEntity::getPhoneNumber)
                .returns("The Falcon of the Millennium", CustomerEntity::getCarModel)
                .returns("1200/615/r115", CustomerEntity::getDimensionOfTires)
                .returns(Collections.emptySet(), CustomerEntity::getOrders);

        CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto("sky", "Luck Waider",
                "5212553535", "Corabl", "123873141/231", Collections.emptyList());

        String requestBody = toJson(customerRequestDto);
        mockMvc.perform(put("/customers")
                        .content(requestBody).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<CustomerEntity> skyCostumer = customersRepository.findByLoginAndDeletedFalse("sky");
            assertThat(skyCostumer)
                    .isPresent()
                    .get()
                    .returns("sky", CustomerEntity::getLogin)
                    .returns("Luck Waider", CustomerEntity::getName)
                    .returns("5212553535", CustomerEntity::getPhoneNumber)
                    .returns("Corabl", CustomerEntity::getCarModel)
                    .returns("123873141/231", CustomerEntity::getDimensionOfTires)
                    .returns(Collections.emptySet(), CustomerEntity::getOrders);
        });
    }
}