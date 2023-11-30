package com.complaints.dto;

import com.complaints.model.Currencies;
import com.complaints.utils.Utc8601InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class PurchaseDto {

    private String id;
    private String userId;
    private String productId;
    private String productName;
    private Double pricePaidAmount;
    private Currencies priceCurrency;
    private Double discountPercent;
    private String merchantId;
    @JsonSerialize(using = Utc8601InstantSerializer.class)
    private Instant purchaseDate;
}
