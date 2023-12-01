package com.complaints.controller;

import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import com.complaints.exceptions.BadRequestException;
import com.complaints.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
@CrossOrigin
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping(path = "/add")
    public ComplaintResponseDto addComplaint(@RequestBody @Valid ComplaintRequestDto complaintRequestDto) {
        try {
            UUID userUuid = UUID.fromString(complaintRequestDto.getUserId());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Bad User Id");
        }
        try {
            UUID purchaseUuid = complaintRequestDto.getPurchaseId() == null ?
                    null : UUID.fromString(complaintRequestDto.getPurchaseId());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Bad Purchase Id");
        }
        return complaintService.addComplaint(complaintRequestDto);
    }

    @GetMapping(path = "/{complaintId}")
    public ComplaintResponseDto service(@PathVariable String complaintId) {
        UUID complaintUuid;
        try {
            complaintUuid = UUID.fromString(complaintId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Bad Complaint Id");
        }
        return complaintService.findComplaintById(complaintUuid);
    }
}
