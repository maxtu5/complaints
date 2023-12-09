package com.complaints.service;

import com.complaints.dao.*;
import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import com.complaints.dto.PurchaseDto;
import com.complaints.dto.UserDto;
import com.complaints.exceptions.BadRequestException;
import com.complaints.exceptions.ItemNotFoundException;
import com.complaints.model.Complaint;
import com.complaints.model.ComplaintStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ExternalApiDataSourcePurchase purchaseDataSource;
    private final ExternalApiDataSourceUser userDataSource;
    private final ModelMapper modelMapper;

    @Override
    public Mono<ComplaintResponseDto> addComplaint(ComplaintRequestDto complaintRequestDto) {
        return Mono.just(complaintRequestDto)
                .map(item -> modelMapper.map(item, ComplaintResponseDto.class))
                .flatMap(this::updateItem)
                .flatMap(this::saveComplaint);
    }

    private Mono<ComplaintResponseDto> saveComplaint(ComplaintResponseDto complaintResponseDto) {
        Complaint complaint = modelMapper.map(complaintResponseDto, Complaint.class);
        complaint.setStatus(ComplaintStatus.STATUS_NEW);
        complaint.setCreateDate(Instant.now());
        return complaintRepository.save(complaint)
                .map(item -> modelMapper.map(item, ComplaintResponseDto.class));
    }

    @Override
    public Mono<ComplaintResponseDto> findComplaintById(Long complaintId) {
        return Mono.just(complaintId)
                .flatMap(complaintRepository::findById)
                .switchIfEmpty(Mono.error(new ItemNotFoundException("Complaint Id not found")))
                .map(item -> modelMapper.map(item, ComplaintResponseDto.class))
                .flatMap(this::updateItem);
    }

    private Mono<ComplaintResponseDto> updateItem(ComplaintResponseDto item) {
        Mono<UserDto> user = userDataSource.findById(item.getUserId());
        Mono<PurchaseDto> purchase = item.getPurchaseId()==null ? 
                Mono.empty() :
                purchaseDataSource.findById(item.getPurchaseId());
        return Mono.zip(Mono.just(item), user, purchase)
                .flatMap(this::updateItem);
    }

    private Mono<ComplaintResponseDto> updateItem(Tuple3<ComplaintResponseDto, UserDto, PurchaseDto> tuple) {
        tuple.getT1().setUser(tuple.getT2());
        tuple.getT1().setPurchase(tuple.getT3());
        return Mono.just(tuple.getT1());
    }

}
