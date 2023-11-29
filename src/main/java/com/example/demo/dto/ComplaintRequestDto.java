package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ComplaintRequestDto {

    @NotBlank(message = "User Id is mandatory")
    private String userId;
    @NotBlank(message = "Subject is mandatory")
    @Size(max = 255, message = "Subject length limit is 255")
    private String subject;
    @NotBlank(message = "Complaint body is mandatory")
    @Size(max = 2000, message = "Complaint body length limit is 2000")
    private String complaint;
    private String purchaseId;
}
