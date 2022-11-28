package hu.bme.aut.controllerservice.security.dto;

import hu.bme.aut.controllerservice.security.entity.Role;
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
