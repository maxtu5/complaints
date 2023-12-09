package com.complaints.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserDto {
    private String id;
    private String fullName;
    private String emailAddress;
    private String physicalAddress;
}
