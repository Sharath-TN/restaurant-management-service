package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;
import com.restaurant.service.MenuItemService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class MenuItemControllerTest {

    @Mock
    private MenuItemService menuItemService;

    private MenuItemController menuItemController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        menuItemController = new MenuItemController(menuItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(menuItemController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testAddMenuItem() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/menuItem/addMenuItem")
                .contentType("application/json")
                .content("{\"id\":1,\"name\":\"Pasta\",\"description\":\"Italian pasta\",\"price\":12.99,\"category\":\"MAIN_COURSE\"}");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(menuItemService).addMenuItem(any(MenuItemDTO.class));
        assertEquals("Menu item added successfully", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetAllMenuItems() throws Exception {
        MenuItem item1 = new MenuItem();
        item1.setId(1L);
        item1.setName("Pasta");
        item1.setPrice(12.99);

        MenuItem item2 = new MenuItem();
        item2.setId(2L);
        item2.setName("Pizza");
        item2.setPrice(14.99);

        List<MenuItem> menuItems = Arrays.asList(item1, item2);

        Mockito.when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

        MockHttpServletRequestBuilder mockRequest = get("/menuItem/getAllMenuItems")
                .contentType("application/json");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        List<MenuItem> responseItems = objectMapper.readValue(responseContent,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MenuItem.class));

        assertEquals(2, responseItems.size());
        assertEquals("Pasta", responseItems.get(0).getName());
        assertEquals("Pizza", responseItems.get(1).getName());
        Mockito.verify(menuItemService).getAllMenuItems();
    }

    @Test
    public void testDeleteMenuItem() throws Exception {
        Long menuItemId = 1L;

        MockHttpServletRequestBuilder mockRequest = delete("/menuItem/deleteMenuItem/{id}", menuItemId)
                .contentType("application/json");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(menuItemService).deleteMenuItem(eq(menuItemId));
        assertEquals("Menu item deleted successfully", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testUpdateMenuItem() throws Exception {
        MockHttpServletRequestBuilder mockRequest = patch("/menuItem/updateMenuItem")
                .contentType("application/json")
                .content("{\"id\":1,\"name\":\"Updated Pasta\",\"description\":\"Updated Italian pasta\",\"price\":13.99,\"category\":\"MAIN_COURSE\"}");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(menuItemService).updateMenuItem(any(MenuItemDTO.class));
        assertEquals("Menu item updated successfully", resultActions.andReturn().getResponse().getContentAsString());
    }
}