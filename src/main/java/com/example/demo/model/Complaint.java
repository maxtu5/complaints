package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Complaint {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID complaintId;
    @NonNull
    private String subject;
    @NonNull
    @Column(length = 2000)
    private String complaint;
    @NonNull
    private Instant createDate;
    @NonNull
    private ComplaintStatus status;
    @NonNull
    private UUID userId;
    private UUID purchaseId;
}
