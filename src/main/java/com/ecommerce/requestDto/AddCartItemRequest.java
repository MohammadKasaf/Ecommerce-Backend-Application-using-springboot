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
public class AddCartItemRequest {

    private Long cartId;
    private Long product_id;
    private Integer quantity;


}
