package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String id;
    private String fullName;
    private String emailAddress;
    private String physicalAddress;
}
