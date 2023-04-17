package com.skipthedishes.courier.controllers;

import java.math.BigDecimal;
import java.util.Collections;

import com.skipthedishes.courier.exceptions.NotFoundException;
import com.skipthedishes.courier.models.DeliveryStatementDto;
import com.skipthedishes.courier.models.DeliveryTransactionDto;
import com.skipthedishes.courier.services.DeliveryTransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
class DeliveryTransactionControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private DeliveryTransactionService deliveryTransactionService;
    @Autowired
    private DeliveryTransactionController deliveryTransactionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryTransactionController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testTransactionsShouldReturn200() throws Exception {
        String start = "1681511049";
        String end = "1681683913";
        given(
            deliveryTransactionService.findTransactionsByCourierAndDate(1, Long.parseLong(start), Long.parseLong(end)))
            .willReturn(Collections.singletonList(new DeliveryTransactionDto(1, BigDecimal.ONE)));

        ResultActions response = mockMvc.perform(get("/delivery/transactions")
            .param("courierId", "1")
            .param("start", start)
            .param("end", end));

        response.andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].deliveryId").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(1));
    }

    @Test
    void testStatementReturn200() throws Exception {
        given(deliveryTransactionService.calculateStatement(1))
            .willReturn(new DeliveryStatementDto(1, BigDecimal.ONE));

        ResultActions response = mockMvc.perform(get("/delivery/statement")
            .param("courierId", "1"));

        response.andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.courierId").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1));
    }

    @Test
    void testStatementReturn400() throws Exception {
        String message = String.format("Delivery with courierId [%s] not found.", 1);
        given(deliveryTransactionService.calculateStatement(1))
            .willReturn(new DeliveryStatementDto(1, BigDecimal.ONE));
        willThrow(new NotFoundException(message)).given(deliveryTransactionService).calculateStatement(1);

        ResultActions response = mockMvc.perform(get("/delivery/statement")
            .param("courierId", "1"));

        response.andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());
    }
}