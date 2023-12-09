package com.complaints.service;

import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ComplaintService {

    Mono<ComplaintResponseDto> addComplaint(ComplaintRequestDto complaintRequestDto);

    Mono<ComplaintResponseDto> findComplaintById(Long complaintId);
}
