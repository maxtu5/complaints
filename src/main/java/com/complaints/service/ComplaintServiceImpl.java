package com.complaints.service;

import com.complaints.dao.ComplaintRepository;
import com.complaints.dao.ExternalApiDataSourcePurchase;
import com.complaints.dao.ExternalApiDataSourceUser;
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
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                .map(cr -> modelMapper.map(cr, Complaint.class))
                .flatMap(this::addExternalsAndSaveIfNew);
    }

    @Override
    public Mono<ComplaintResponseDto> findComplaintById(UUID complaintId) {
        return Mono.just(complaintId)
                .flatMap(complaintRepository::findById)
                .switchIfEmpty(Mono.error(new ItemNotFoundException("Complaint Id not found")))
                .flatMap(this::addExternalsAndSaveIfNew);
    }

    private Mono<ComplaintResponseDto> addExternalsAndSaveIfNew(Complaint complaint) {
        Mono<Optional<UserDto>> user = userDataSource.findById(complaint.getUserId());
        Mono<Optional<PurchaseDto>> purchase = complaint.getPurchaseId() == null ?
                Mono.just(Optional.empty()) : purchaseDataSource.findById(complaint.getPurchaseId());
        return Mono.zip(Mono.just(complaint), user, purchase)
                .flatMap(this::addExternalsAndSaveIfNew);
    }

    private Mono<ComplaintResponseDto> addExternalsAndSaveIfNew(Tuple3<Complaint, Optional<UserDto>, Optional<PurchaseDto>> tuple) {
        Complaint c = tuple.getT1();
        boolean newComplaint = c.getComplaintId() == null;
        UserDto u = tuple.getT2().orElse(null);
        PurchaseDto p = tuple.getT3().orElse(null);
        List<String> errors = validateUserPurchase(c, u, p);
        if (errors.size() > 0) {
            return Mono.error(newComplaint ?
                    new BadRequestException(errors) : new InternalServerErrorException(errors));
        }
        if (newComplaint) {
            c.setStatus(ComplaintStatus.STATUS_NEW);
            c.setCreateDate(Instant.now());
        }
        Mono<Complaint> ret = newComplaint ?
                complaintRepository.save(c) : Mono.just(c);
        return ret.map(cr -> modelMapper.map(cr, ComplaintResponseDto.class))
                .flatMap(cr -> addExternals(cr, u, p));
    }

    private List<String> validateUserPurchase(Complaint c, UserDto u, PurchaseDto p) {
        List<String> errors = new ArrayList<>();
        if (u == null) {
            errors.add("User Id not found");
        }
        if (c.getPurchaseId() != null && p == null) {
            errors.add("Purchase Id not found");
        }
        if (p != null && u!=null && !u.getId().equals(p.getUserId())) {
            errors.add("User Id and Purchase Id don't match");
        }
        return errors;
    }

    private Mono<ComplaintResponseDto> addExternals(ComplaintResponseDto cr, UserDto u, PurchaseDto p) {
        cr.setUser(u);
        cr.setPurchase(p);
        return Mono.just(cr);
    }

}
