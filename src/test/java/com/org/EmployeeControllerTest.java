package com.org;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void createsEmployeeWithManager() throws Exception {
        String boss = """
            {"firstName":"Sara","lastName":"Khan","dateOfBirth":"1985-03-10","department":"IT","salary":20000}
            """;
        String body = mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(boss))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String bossId = body.replaceAll(".*\"id\":(\\d+).*", "$1");

        String emp = """
            {"firstName":"Ali","lastName":"Ahmed","dateOfBirth":"1995-07-01","department":"IT","salary":9000,"managerId":%s}
            """.formatted(bossId);
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(emp))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.managerName").value("Sara Khan"));
    }

    @Test
    void rejectsMissingFields() throws Exception {
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.salary").exists());
    }

    @Test
    void returns404ForUnknownId() throws Exception {
        mvc.perform(get("/api/employees/9999")).andExpect(status().isNotFound());
    }
}
