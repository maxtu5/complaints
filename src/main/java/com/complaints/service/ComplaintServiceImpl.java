package com.complaints.service;

import com.complaints.dao.ComplaintRepository;
import com.complaints.dao.ExternalApiDataSource;
import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import com.complaints.dto.PurchaseDto;
import com.complaints.dto.UserDto;
import com.complaints.exceptions.BadRequestException;
import com.complaints.exceptions.InternalServerErrorException;
import com.complaints.exceptions.ItemNotFoundException;
import com.complaints.model.Complaint;
import com.complaints.model.ComplaintStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ExternalApiDataSource<PurchaseDto> purchaseDataSource;
    private final ExternalApiDataSource<UserDto> userDataSource;
    private final ModelMapper modelMapper;

    @Override
    public ComplaintResponseDto addComplaint(ComplaintRequestDto complaintRequestDto) {
        UserDto user;
        user = userDataSource.findById(UUID.fromString(complaintRequestDto.getUserId())).orElseThrow(() -> new ItemNotFoundException("User Id not found"));
        PurchaseDto purchase = null;
        if (complaintRequestDto.getPurchaseId() != null) {
            UUID purchaseUuid = UUID.fromString(complaintRequestDto.getPurchaseId());
            purchase = purchaseDataSource.findById(purchaseUuid).orElseThrow(() -> new ItemNotFoundException("Purchase Id not found"));
            if (!purchase.getUserId().equals(user.getId())) {
                throw new BadRequestException("Purchase Id does not belong to User Id");
            }
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
                .orElseThrow(() -> new ItemNotFoundException("Complaint Id not found"));
        ComplaintResponseDto complaintResponseDto = modelMapper.map(complaint, ComplaintResponseDto.class);
        UserDto user = userDataSource.findById(complaint.getUserId())
                .orElseThrow(() -> new InternalServerErrorException("Wrong userId in complaint"));
        complaintResponseDto.setUser(user);
        if (complaint.getPurchaseId() != null) {
            PurchaseDto purchase = purchaseDataSource.findById(complaint.getPurchaseId())
                    .orElseThrow(() -> new InternalServerErrorException("Wrong purchaseId in complaint"));
            complaintResponseDto.setPurchase(purchase);
        }
        complaintResponseDto.setUser(modelMapper.map(user, UserDto.class));
        return complaintResponseDto;
    }

}
