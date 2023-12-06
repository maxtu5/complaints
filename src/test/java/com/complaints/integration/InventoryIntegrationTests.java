package com.complaints.integration;

import com.complaints.Demo1Application;
import com.complaints.dao.ComplaintRepository;
import com.complaints.dao.ExternalApiDataSourcePurchase;
import com.complaints.dao.ExternalApiDataSourceUser;
import com.complaints.dto.ComplaintRequestDto;
import com.complaints.dto.ComplaintResponseDto;
import com.complaints.dto.PurchaseDto;
import com.complaints.dto.UserDto;
import com.complaints.model.Complaint;
import com.complaints.model.ComplaintStatus;
import com.complaints.model.Currencies;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Demo1Application.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InventoryIntegrationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ExternalApiDataSourcePurchase purchaseDataSource;
    @Autowired
    private ExternalApiDataSourceUser userDataSource;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

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
    public void testAddGet_ValidRequest() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/complaints/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseString = mvcResult.getResponse().getContentAsString();
        ComplaintResponseDto actualResponse = objectMapper.readValue(actualResponseString, ComplaintResponseDto.class);
        UUID userUuid = UUID.fromString(actualResponse.getComplaintId());
        assertThat(actualResponse.getSubject()).isEqualTo(expectedResponse.getSubject());
        assertThat(actualResponse.getCreateDate().truncatedTo(ChronoUnit.DAYS)).isEqualTo(Instant.now().truncatedTo(ChronoUnit.DAYS));
        assertThat(ComplaintStatus.valueOf(actualResponse.getStatus())).isEqualTo(ComplaintStatus.valueOf(expectedResponse.getStatus()));
        assertThat(actualResponse.getUser().getId()).isEqualTo(expectedResponse.getUser().getId());
        assertThat(actualResponse.getPurchase().getId()).isEqualTo(expectedResponse.getPurchase().getId());

        mvcResult = mvc.perform(get("/complaints/"+actualResponse.getComplaintId()))
                .andExpect(status().isOk())
                .andReturn();
        actualResponseString = mvcResult.getResponse().getContentAsString();
        actualResponse = objectMapper.readValue(actualResponseString, ComplaintResponseDto.class);
        userUuid = UUID.fromString(actualResponse.getComplaintId());
        assertThat(actualResponse.getSubject()).isEqualTo(expectedResponse.getSubject());
        assertThat(actualResponse.getCreateDate().truncatedTo(ChronoUnit.DAYS)).isEqualTo(Instant.now().truncatedTo(ChronoUnit.DAYS));
        assertThat(ComplaintStatus.valueOf(actualResponse.getStatus())).isEqualTo(ComplaintStatus.valueOf(expectedResponse.getStatus()));
        assertThat(actualResponse.getUser().getId()).isEqualTo(expectedResponse.getUser().getId());
        assertThat(actualResponse.getPurchase().getId()).isEqualTo(expectedResponse.getPurchase().getId());
    }
}
