package com.complaints.controller;

import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import com.complaints.exceptions.BadRequestException;
import com.complaints.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ReactiveConroller {

    private final ComplaintService complaintService;

    @GetMapping("/")
    public Mono<String> getIndex() {
        return Mono.just("Hello World!");
    }

    @PostMapping(path = "/add")
    public Mono<ComplaintResponseDto> addComplaint(@RequestBody @Valid ComplaintRequestDto complaintRequestDto) {
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
    public Mono<ComplaintResponseDto> service(@PathVariable Long complaintId) {
//        UUID complaintUuid;
//        try {
//            complaintUuid = UUID.fromString(complaintId);
//        } catch (IllegalArgumentException e) {
//            throw new BadRequestException("Bad Complaint Id");
//        }
        return complaintService.findComplaintById(complaintId);
    }


}
