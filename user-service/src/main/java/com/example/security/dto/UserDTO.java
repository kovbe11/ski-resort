package com.example.security.dto;

import com.example.security.entity.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;

    private String username;
    private String password;
    private boolean enabled;
    private Set<Role> roles = new HashSet<>();
}
