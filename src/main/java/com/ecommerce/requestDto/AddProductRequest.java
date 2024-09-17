package com.ecommerce.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductRequest {

    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Long categoryId;
}
