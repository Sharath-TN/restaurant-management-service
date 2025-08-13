package com.restaurant.service;

import com.restaurant.dto.LoginDTO;
import com.restaurant.dto.RegisterDTO;

public interface AuthService {
    void registerAdmin(RegisterDTO request);

    void registerRestaurantEmployee(RegisterDTO request);

    String login(LoginDTO request);
}
