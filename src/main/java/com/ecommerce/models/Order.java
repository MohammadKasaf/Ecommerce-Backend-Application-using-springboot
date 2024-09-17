package com.ecommerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",cascade =CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
    private String shippingAddress;
    private LocalDateTime deliveryDate;
}
