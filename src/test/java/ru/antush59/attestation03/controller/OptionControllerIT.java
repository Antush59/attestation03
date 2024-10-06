package ru.antush59.attestation03.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import ru.antush59.attestation03.BaseIT;
import ru.antush59.attestation03.controller.dto.request.CustomerRequestDto;
import ru.antush59.attestation03.controller.dto.request.OptionRequestDto;
import ru.antush59.attestation03.controller.dto.response.CustomerResponseDto;
import ru.antush59.attestation03.controller.dto.response.OptionResponseDto;
import ru.antush59.attestation03.controller.dto.update.CustomerUpdateRequestDto;
import ru.antush59.attestation03.controller.dto.update.OptionUpdateRequestDto;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.repository.OptionsRepository;

import java.math.BigDecimal;
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

public class OptionControllerIT extends BaseIT {
    @Autowired
    private OptionsController optionsController;

    @Autowired
    private OptionsRepository optionsRepository;

    @Test
    @Sql(value = "/db/scripts/create_options.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnOptions() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/options"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();

        String content = response.getContentAsString();

        assertNotNull(content);

        List<OptionResponseDto> options = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(options);

        assertEquals(1, options.size());

        assertThat(options.get(0))
                .isNotNull()
                .returns("option1", OptionResponseDto::getName)
                .returns(new BigDecimal("1234.20"), OptionResponseDto::getPrice);
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_options.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldReturnByNameOption() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/options")
                        .param("name", "option2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();

        String content = response.getContentAsString();

        assertNotNull(content);

        List<OptionResponseDto> options = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertNotNull(options);

        assertEquals(1, options.size());

        assertThat(options.get(0))
                .isNotNull()
                .returns("option2", OptionResponseDto::getName)
                .returns(new BigDecimal("1298.40"), OptionResponseDto::getPrice);
    }

    @Test
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldSaveOption() throws Exception {
        Optional<OptionEntity> getOption = optionsRepository.findByNameAndDeletedFalse("option4");
        assertTrue(getOption.isEmpty());

        OptionRequestDto optionRequestDto = new OptionRequestDto();
        optionRequestDto.setName("option4");
        optionRequestDto.setPrice(BigDecimal.valueOf(4560.50));
        String requestBody = toJson(optionRequestDto);
        mockMvc.perform(post("/options")
                        .content(requestBody).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<OptionEntity> antCustomer = optionsRepository.findByNameAndDeletedFalse("option4");
            assertThat(antCustomer)
                    .isPresent()
                    .get()
                    .returns("option4", OptionEntity::getName)
                    .returns(new BigDecimal("4560.50"), OptionEntity::getPrice);
        });
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_options.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteOption() throws Exception {
        Optional<OptionEntity> option = optionsRepository.findByNameAndDeletedFalse("option3");
        assertTrue(option.isPresent());

        mockMvc.perform(delete("/options")
                        .param("name", "option3"))
                .andExpect(status().isNoContent())
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<OptionEntity> optionDel = optionsRepository.findByNameAndDeletedFalse("option3");
            assertThat(optionDel)
                    .isEmpty();
        });
    }

    @Test
    @Sql(value = "/db/scripts/create_multiple_options.sql")
    @Sql(value = "/db/scripts/truncate_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpdateOption() throws Exception {
        Optional<OptionEntity> option = optionsRepository.findByNameAndDeletedFalse("option3");
        assertThat(option)
                .isPresent()
                .get()
                .returns("option3", OptionEntity::getName)
                .returns( new BigDecimal("348.10"), OptionEntity::getPrice);

        OptionUpdateRequestDto optionUpdateRequestDto = new OptionUpdateRequestDto( 2L,"option3",
                new BigDecimal("4386.35")) ;

        String requestBody = toJson(optionUpdateRequestDto);
        mockMvc.perform(put("/options")
                        .content(requestBody).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());

        transactionTemplate.executeWithoutResult(tx -> {
            Optional<OptionEntity> optionEntity = optionsRepository.findByNameAndDeletedFalse("option3");
            assertThat(optionEntity)
                    .isPresent()
                    .get()
                    .returns("option3", OptionEntity::getName)
                    .returns(new BigDecimal("4386.35"), OptionEntity::getPrice);
        });
    }
}
