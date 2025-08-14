package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.dto.LoginDTO;
import com.restaurant.dto.RegisterDTO;
import com.restaurant.service.AuthService;
import com.restaurant.util.JwtUtil;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    private AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        authController = new AuthController(authService, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testRegisterAdmin() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/auth/registerAdmin")
                .contentType("application/json")
                .content("{\"firstName\":\"Admin\",\"lastName\":\"User\",\"email\":\"admin@gmail.com\",\"password\":\"adminPass123\",\"role\":\"ADMIN\"}");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(authService).registerAdmin(Mockito.any(RegisterDTO.class));
        assertEquals("Admin registered successfully", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testLogin() throws Exception {
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.Gfx6LkvwZRjHJK4G";
        Mockito.when(authService.login(Mockito.any(LoginDTO.class))).thenReturn(mockToken);

        MockHttpServletRequestBuilder mockRequest = get("/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"admin@gmail.com\",\"password\":\"adminPass123\"}");

        ResultActions resultActions = this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(authService).login(Mockito.any(LoginDTO.class));
        assertEquals(mockToken, resultActions.andReturn().getResponse().getContentAsString());
    }
}