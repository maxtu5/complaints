package com.example.demo.service;

import com.example.demo.dao.ComplaintRepository;
import com.example.demo.dao.ExternalApiDataSource;
import com.example.demo.dto.ComplaintRequestDto;
import com.example.demo.dto.ComplaintResponseDto;
import com.example.demo.dto.PurchaseDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Complaint;
import com.example.demo.model.ComplaintStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ExternalApiDataSource<PurchaseDto> purchaseDataSource;
    private final ExternalApiDataSource<UserDto> userDataSource;
    private final ModelMapper modelMapper;

    @Override
    public ComplaintResponseDto addComplaint(ComplaintRequestDto complaintRequestDto) {
        UserDto user;
        user = userDataSource.findById(UUID.fromString(complaintRequestDto.getUserId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not found"));
        PurchaseDto purchase = null;
        try {
            if (complaintRequestDto.getPurchaseId() != null) {
                UUID purchaseUuid = UUID.fromString(complaintRequestDto.getPurchaseId());
                purchase = purchaseDataSource.findById(purchaseUuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase Id not found"));
                if (!purchase.getUserId().equals(user.getId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Id does not belong to User Id");
                }
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Purchase Id format");
        }
        Complaint complaint = modelMapper.map(complaintRequestDto, Complaint.class);
        complaint.setStatus(ComplaintStatus.STATUS_NEW);
        complaint.setCreateDate(Instant.now());
        complaint = complaintRepository.save(complaint);
        ComplaintResponseDto complaintResponseDto = modelMapper.map(complaint, ComplaintResponseDto.class);
        complaintResponseDto.setUser(user);
        if (purchase != null) {
            complaintResponseDto.setPurchase(purchase);
        }
        return complaintResponseDto;
    }

    @Override
    public ComplaintResponseDto findComplaintById(UUID complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint Id not found"));
        ComplaintResponseDto complaintResponseDto = modelMapper.map(complaint, ComplaintResponseDto.class);
        UserDto user = userDataSource.findById(complaint.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving purchase data"));
        complaintResponseDto.setUser(user);
        if (complaint.getPurchaseId() != null) {
            PurchaseDto purchase = purchaseDataSource.findById(complaint.getPurchaseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving purchase data"));
            complaintResponseDto.setPurchase(purchase);
        }
        complaintResponseDto.setUser(modelMapper.map(user, UserDto.class));
        return complaintResponseDto;
    }

}
