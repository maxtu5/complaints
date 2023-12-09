package com.complaints.dto;

import com.complaints.utils.Utc8601InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class ComplaintResponseDto {
    private String complaintId;
    private String subject;
    private String complaint;
    @JsonSerialize(using = Utc8601InstantSerializer.class)
    private Instant createDate;
    private String status;
    private UserDto user;
    private PurchaseDto purchase;
    private UUID userId;
    private UUID purchaseId;
}
