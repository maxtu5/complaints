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
public class ComplaintConroller {

    private final ComplaintService complaintService;

    @PostMapping(path = "/add")
    public Mono<ComplaintResponseDto> addComplaint(@RequestBody @Valid ComplaintRequestDto complaintRequestDto) {
        return Mono.just(complaintRequestDto)
                .map(this::validateUuids)
                .flatMap(complaintService::addComplaint);
    }

    @GetMapping(path = "/{complaintId}")
    public Mono<ComplaintResponseDto> service(@PathVariable String complaintId) {
        return Mono.just(complaintId)
                .map(UUID::fromString)
                .onErrorResume(e -> Mono.error(new BadRequestException("Bad Complaint Id")))
                .flatMap(complaintService::findComplaintById);
    }

    private ComplaintRequestDto validateUuids(ComplaintRequestDto complaintRequestDto) {
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
        return complaintRequestDto;
    }

}
