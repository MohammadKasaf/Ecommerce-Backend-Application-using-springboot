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
public class GetUserResponse {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private String address;
    private String phoneNumber;
}
