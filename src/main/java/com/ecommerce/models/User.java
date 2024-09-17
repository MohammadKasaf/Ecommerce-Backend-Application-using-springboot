package com.ecommerce.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String role;
    private String address;
    private String phoneNumber;

    @OneToMany(mappedBy = "user",cascade =CascadeType.ALL)
    private List<Order> orderList;

    @OneToMany(mappedBy = "user",cascade =CascadeType.ALL)
    private List<Review> reviewList;

    @OneToOne(mappedBy = "user",cascade =CascadeType.ALL)
    private ShoppingCart shoppingCart;


}
