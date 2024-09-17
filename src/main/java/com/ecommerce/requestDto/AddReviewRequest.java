package com.ecommerce.requestDto;

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
public class AddReviewRequest {

    private Long userId;
    private Long productId;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
}
