package com.ecommerce.responseDto;

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
public class GetCartItemResponse {

    private Long cartItemId;
    private String productName;
    private Integer quantity;
    private Double price;


}
