package com.ecommerce.repositories;

import com.ecommerce.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByShoppingCart_CartId(Long cartId);

}
