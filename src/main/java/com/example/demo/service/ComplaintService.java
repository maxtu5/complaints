package com.example.demo.service;

import com.example.demo.dto.ComplaintRequestDto;
import com.example.demo.dto.ComplaintResponseDto;

import java.util.UUID;

public interface ComplaintService {

    ComplaintResponseDto addComplaint(ComplaintRequestDto complaintRequestDto);

    ComplaintResponseDto findComplaintById(UUID complaintId);
}
