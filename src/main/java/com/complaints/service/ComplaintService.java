package com.complaints.service;

import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;

import java.util.UUID;

public interface ComplaintService {

    ComplaintResponseDto addComplaint(ComplaintRequestDto complaintRequestDto);

    ComplaintResponseDto findComplaintById(UUID complaintId);
}
