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
public class GetReviewResponse {

    private String username;
    private String productName;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
}
