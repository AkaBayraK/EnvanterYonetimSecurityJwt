package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto
{
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}