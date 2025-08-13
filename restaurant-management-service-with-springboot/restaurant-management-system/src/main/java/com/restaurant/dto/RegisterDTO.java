package com.restaurant.dto;

import com.zeta_training.restaurant_management_system.enumeration.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
