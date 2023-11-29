package com.example.demo.dto;

import com.example.demo.model.Currencies;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
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
//    @JsonDeserialize(using = Utc8601OffsetDateTimeDeserializer.class)
    @JsonSerialize(using = InstantSerializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING )
    private Instant purchaseDate;
}
