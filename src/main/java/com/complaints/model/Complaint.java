package com.complaints.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.UUID;

//@Entity
@Table
@Setter
@Getter
public class Complaint {
    @Id
    private UUID complaintId;
    @NonNull
    private String subject;
    @NonNull
//    @Column(length = 2000)
    private String complaint;
    @NonNull
    private Instant createDate;
    @NonNull
    private ComplaintStatus status;
    @NonNull
    private UUID userId;
    private UUID purchaseId;
}
