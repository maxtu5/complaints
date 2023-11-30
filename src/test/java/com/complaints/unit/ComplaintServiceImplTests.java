package com.complaints.unit;

import com.complaints.dao.ComplaintRepository;
import com.complaints.dao.ExternalApiDataSource;
import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import com.complaints.dto.PurchaseDto;
import com.complaints.dto.UserDto;
import com.complaints.model.Complaint;
import com.complaints.model.ComplaintStatus;
import com.complaints.model.Currencies;
import com.complaints.service.ComplaintService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComplaintServiceImplTests {

    @MockBean
    private ComplaintRepository complaintRepository;
    @MockBean
    private ExternalApiDataSource<UserDto> userDtoExternalApiDataSource;
    @MockBean
    private ExternalApiDataSource<PurchaseDto> purchaseDtoExternalApiDataSource;
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    ComplaintRequestDto request;
    ComplaintResponseDto expectedResponse;
    UserDto user;
    PurchaseDto purchase;
    Complaint complaint;

    @BeforeAll
    void setup() {
        complaint = new Complaint();
        complaint.setUserId(UUID.fromString("f22dff3f-06cf-49fe-97ec-bf7afe9a7fdb"));
        complaint.setSubject("The seller never sent my item!!");
        complaint.setComplaint("I made a purchase and the item hasn’t shipped. It’s been over a week. Please help!");
        complaint.setPurchaseId(UUID.fromString("4933d1ce-9ca7-4640-ba17-e442057c44f1"));
        complaint.setCreateDate(Instant.now());
        complaint.setStatus(ComplaintStatus.STATUS_NEW);

        user = new UserDto();
        user.setId("f22dff3f-06cf-49fe-97ec-bf7afe9a7fdb");
        user.setFullName("Florence Hart");
        user.setEmailAddress("florencehart@test.com");
        user.setPhysicalAddress("Test Lane 3 Los Angeles");

        purchase = new PurchaseDto();
        purchase.setId("4933d1ce-9ca7-4640-ba17-e442057c44f1");
        purchase.setUserId("f22dff3f-06cf-49fe-97ec-bf7afe9a7fdb");
        purchase.setProductId("114449b0-41e7-44ac-ac0c-0a0433be9801");
        purchase.setProductName("Laptop");
        purchase.setPricePaidAmount(3000.0);
        purchase.setPriceCurrency(Currencies.USD);
        purchase.setDiscountPercent(0.0);
        purchase.setMerchantId("232ee131-16ee-4185-9086-600fae08a880");
        purchase.setPurchaseDate(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse("2021-10-20T21:00:00.000+00:00", Instant::from));

        request = modelMapper.map(complaint, ComplaintRequestDto.class);

        expectedResponse = modelMapper.map(complaint, ComplaintResponseDto.class);
        expectedResponse.setUser(user);
        expectedResponse.setPurchase(purchase);
    }

    @Test
    void addComplaint_ValidData() throws JsonProcessingException {
        given(userDtoExternalApiDataSource.findById(any(UUID.class))).willReturn(Optional.of(user));
        given(purchaseDtoExternalApiDataSource.findById(any(UUID.class))).willReturn(Optional.of(purchase));
        given(complaintRepository.save(any(Complaint.class))).willReturn(complaint);
        String actualResponseString = objectMapper.writeValueAsString(complaintService.addComplaint(request));
        ComplaintResponseDto actualResponse = objectMapper.readValue(actualResponseString, ComplaintResponseDto.class);
        assertThat(actualResponse.getSubject()).isEqualTo(expectedResponse.getSubject());
        assertThat(actualResponse.getCreateDate().truncatedTo(ChronoUnit.DAYS)).isEqualTo(Instant.now().truncatedTo(ChronoUnit.DAYS));
        assertThat(ComplaintStatus.valueOf(actualResponse.getStatus())).isEqualTo(ComplaintStatus.valueOf(expectedResponse.getStatus()));
        assertThat(actualResponse.getUser().getId()).isEqualTo(user.getId());
        assertThat(actualResponse.getPurchase().getId()).isEqualTo(purchase.getId());
    }

    @Test
    void addComplaint_InValidUserId() {
        given(userDtoExternalApiDataSource.findById(any(UUID.class))).willReturn(Optional.of(user));
        try {
            complaintService.addComplaint(request);
            fail("");
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(ResponseStatusException.class);
        }

    }
}
