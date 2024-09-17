package com.ecommerce.repositories;

import com.ecommerce.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByProductProductId(Long productId);

    List<Review> findByUserUserId(Long userId);


}
