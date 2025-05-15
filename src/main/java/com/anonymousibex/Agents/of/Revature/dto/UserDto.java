package com.anonymousibex.Agents.of.Revature.dto;

import com.anonymousibex.Agents.of.Revature.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private Role role;
}
