package com.ecommerce.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderResponse {

    private Long orderId;
    private String username;
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
    private String shippingAddress;
    private LocalDateTime deliveryDate;
}
