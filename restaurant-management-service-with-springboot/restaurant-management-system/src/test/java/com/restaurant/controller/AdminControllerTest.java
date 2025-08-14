package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.dto.BillResponseDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.service.AuthService;
import com.restaurant.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class AdminControllerTest {

    @Mock
    private AuthService authService;
    @Mock
    private OrderService orderService;

    private AdminController adminController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        adminController = new AdminController(authService, orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testRegisterRestaurantEmployee() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/admin/registerRestaurantEmployee")
                .contentType("application/json")
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"John@gmail.com\",\"password\":\"password123\",\"role\":\"CHEF\"}");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
        Mockito.verify(authService).registerRestaurantEmployee(Mockito.any(RegisterDTO.class));
        assertEquals("Restaurant employee registered successfully", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGenerateBill() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/admin/generateBill/{orderId}",123)
                .contentType("application/json");
        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setOrderId(123L);
        billResponseDTO.setTotalAmount(100.0);
        billResponseDTO.setGeneratedAt(LocalDateTime.now());
        billResponseDTO.setOrderItems(null);// Assuming order items are not needed for this test

        Mockito.when(orderService.generateBill(123L)).thenReturn(billResponseDTO);
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        String responseContent = resultActions.andReturn().getResponse().getContentAsString();

        BillResponseDTO responseDTO = objectMapper.readValue(responseContent, BillResponseDTO.class);
        assertEquals(123L, responseDTO.getOrderId());
        assertEquals(100.0, responseDTO.getTotalAmount());
        assertEquals(billResponseDTO.getGeneratedAt(), responseDTO.getGeneratedAt());
        Mockito.verify(orderService).generateBill(123L);
    }
}

