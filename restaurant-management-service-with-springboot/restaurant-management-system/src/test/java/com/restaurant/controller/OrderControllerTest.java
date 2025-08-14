package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.dto.OrderRequest;
import com.restaurant.dto.StatusDTO;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.OrderItemStatus;
import com.restaurant.enumeration.OrderStatus;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private OrderController orderController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testPlaceOrder() throws Exception {
        // Create sample request JSON
        String orderRequestJson = "{\"tableBookingId\":1,\"items\":[{\"menuItemId\":1,\"quantity\":2},{\"menuItemId\":3,\"quantity\":1}]}";

        // Create a mock order response
        Order mockOrder = new Order();
        mockOrder.setId(100L);
        mockOrder.setStatus(OrderStatus.PLACED);

        // Setup table booking
        TableBooking tableBooking = new TableBooking();
        tableBooking.setId(1L);
        mockOrder.setTableBooking(tableBooking);

        // Setup order items
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1L);
        orderItems.add(item);
        mockOrder.setOrderItems(orderItems);

        // Mock the service call
        Mockito.when(orderService.placeOrUpdateOrder(any(OrderRequest.class))).thenReturn(mockOrder);

        // Create the request
        MockHttpServletRequestBuilder mockRequest = post("/orders/place")
                .contentType("application/json")
                .content(orderRequestJson);

        // Perform the request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        Order responseOrder = objectMapper.readValue(responseContent, Order.class);

        assertEquals(100L, responseOrder.getId());
        assertEquals(OrderStatus.PLACED, responseOrder.getStatus());
        assertEquals(1L, responseOrder.getOrderItems().size());

        // Verify service was called
        Mockito.verify(orderService).placeOrUpdateOrder(any(OrderRequest.class));
    }

    @Test
    public void testUpdateOrderItem() throws Exception {
        // Create a status update request
        String statusDtoJson = "{\"status\":\"PREPARED\"}";
        Long orderItemId = 123L;

        // Create the request
        MockHttpServletRequestBuilder mockRequest = put("/orders/updateOrderItem/{orderItemId}", orderItemId)
                .contentType("application/json")
                .content(statusDtoJson);

        // Perform the request
        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        // Verify response
        assertEquals("Order item status updated successfully", resultActions.andReturn().getResponse().getContentAsString());

        // Verify service was called with correct parameters
        Mockito.verify(orderService).updateOrderItemStatus(eq(orderItemId), eq(OrderItemStatus.PREPARED));
    }
}