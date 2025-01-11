package com.tdedsh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class UserDto {
    private Integer id; // Use Integer to allow null values
    private String username;
    private String email;
    private String passwordHash;
}
