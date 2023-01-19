package com.pyurtaev.banking.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pyurtaev.banking.api.dto.MoneyTransferDto;
import com.pyurtaev.banking.api.dto.UserDto;
import com.pyurtaev.banking.model.User;
import com.pyurtaev.banking.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.6")
            .withDatabaseName("banking_test_db")
            .withUsername("banking_test_db_username")
            .withPassword("banking_test_db_password");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @SneakyThrows
    @Test
    void searchUsers() {
        final List<User> all = userRepository.findAll();
        Assertions.assertFalse(all.isEmpty());
        Assertions.assertEquals(all.size(), 2);

        final String jwtToken = mockMvc.perform(
                        get("/token")
                                .header("email", "mail@mail.mail")
                                .header("password", "test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final MvcResult result =
                mockMvc
                        .perform(
                                get("/user?page=0&size=20&name=Ja&email=mail@mail.mail")
                                        .header("Authorization", "Bearer " + jwtToken)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();


        final JsonNode content = objectMapper.readTree(result.getResponse().getContentAsString()).get("content");
        Assertions.assertNotNull(content);
        Assertions.assertEquals(content.size(), 1);

        final UserDto actualUser = objectMapper.readValue(content.get(0).toString(), UserDto.class);
        final UserDto expectedUser = UserDto.builder()
                .id(1L)
                .name("Jack")
                .dateOfBirth(LocalDate.of(1993, 5, 1))
                .build();
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @SneakyThrows
    @Test
    void transferMoney() {
        User user1 = userRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user1.getAccount().getBalance(), BigDecimal.valueOf(100));

        User user2 = userRepository.findById(2L).orElse(null);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals(user2.getAccount().getBalance(), BigDecimal.valueOf(200));


        final String jwtToken = mockMvc.perform(
                        get("/token")
                                .header("email", "mail@mail.mail")
                                .header("password", "test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final MoneyTransferDto dto = new MoneyTransferDto();
        dto.setUserIdTransferTo(2L);
        dto.setValue(BigDecimal.valueOf(50));

        mockMvc
                .perform(
                        put("/user/transfer_money")
                                .header("Authorization", "Bearer " + jwtToken)
                                .content(objectMapper.writeValueAsBytes(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        user1 = userRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user1.getAccount().getBalance(), BigDecimal.valueOf(50));

        user2 = userRepository.findById(2L).orElse(null);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals(user2.getAccount().getBalance(), BigDecimal.valueOf(250));

    }
}