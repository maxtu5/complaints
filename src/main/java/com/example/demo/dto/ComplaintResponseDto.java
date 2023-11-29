package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class ComplaintResponseDto {
    String complaintId;
    String subject;
    String complaint;
    Instant createDate;
    String status;
    UserDto user;
    PurchaseDto purchase;
}
