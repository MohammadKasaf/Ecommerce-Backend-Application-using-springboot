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
public class GetProductResponse {

    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Long categoryId;
}
