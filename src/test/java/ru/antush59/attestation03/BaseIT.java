package ru.antush59.attestation03;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class BaseIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @SneakyThrows
    protected String toJson(Object o) {
        return objectMapper.writeValueAsString(o);
    }

}