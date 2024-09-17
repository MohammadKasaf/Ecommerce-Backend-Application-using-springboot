package com.ecommerce.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderHistoryResponse {

    private Long orderId;
    private LocalDateTime orderDate;
    private HashMap<String,Double> products;
    private String status;
    private Double totalAmount;
    private String shippingAddress;
    private LocalDateTime deliveryDate;
}
