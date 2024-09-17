package com.ecommerce.services;

import com.ecommerce.models.*;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.ReviewRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.requestDto.AddReviewRequest;
import com.ecommerce.requestDto.DeleteReviewRequest;
import com.ecommerce.responseDto.GetReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;
    // Create review
    @Transactional
    public String addReview(AddReviewRequest request) {


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + request.getProductId()));


        List<Order> userOrders = user.getOrderList();

        if (userOrders.isEmpty()) {
            return "No orders found for this user.";
        }


        boolean hasOrdered = false;
        boolean isDelivered = false;

        for (Order order : userOrders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                if (orderItem.getProduct().getProductId().equals(product.getProductId())) {
                    hasOrdered = true;
                    if ("Delivered".equalsIgnoreCase(order.getStatus())) {
                        isDelivered = true;
                    }
                    break;
                }
            }
            if (hasOrdered) {
                break;
            }
        }

        // Check if the user has ordered the product
        if (!hasOrdered) {
            throw new RuntimeException("User has not ordered this product, so they cannot review it.");
        }

        // Check if the order status is "Delivered"
        if (!isDelivered) {
            throw new RuntimeException("User cannot review this product as the order status is not 'Delivered'.");
        }

        // Create and save the review
        Review review = Review.builder()
                .user(user)
                .product(product)
                .reviewDate(LocalDateTime.now())
                .comment(request.getComment())
                .rating(request.getRating())
                .build();

        // Save the review in the database
        reviewRepository.save(review);

        return "Review added successfully!";
    }


    public List<GetReviewResponse> getReviewsByProductId(Long productId) {
        // Fetch all reviews for the given product ID
        List<Review> reviews = reviewRepository.findByProductProductId(productId);

        // Create an empty list to hold GetReviewResponse objects
        List<GetReviewResponse> responseList = new ArrayList<>();

        // Manually map each Review entity to GetReviewResponse DTO
        for (Review review : reviews) {
            GetReviewResponse response = new GetReviewResponse();
            response.setUsername(review.getUser().getUsername()); // Assuming Review has a User entity with username
            response.setProductName(review.getProduct().getName()); // Assuming Review has a Product entity with name
            response.setRating(review.getRating());
            response.setComment(review.getComment());
            response.setReviewDate(review.getReviewDate());

            // Add the mapped response object to the list
            responseList.add(response);
        }

        return responseList; // Return the list of responses
    }

    public List<GetReviewResponse> getReviewsByUserId(Long userId) {
        // Fetch all reviews for the given user ID
        List<Review> reviews = reviewRepository.findByUserUserId(userId);

        // Create an empty list to hold GetReviewResponse objects
        List<GetReviewResponse> responseList = new ArrayList<>();

        // Manually map each Review entity to GetReviewResponse DTO
        for (Review review : reviews) {
            GetReviewResponse response = new GetReviewResponse();
            response.setUsername(review.getUser().getUsername()); // Assuming Review has a User entity with username
            response.setProductName(review.getProduct().getName()); // Assuming Review has a Product entity with name
            response.setRating(review.getRating());
            response.setComment(review.getComment());
            response.setReviewDate(review.getReviewDate());

            // Add the mapped response object to the list
            responseList.add(response);
        }

        return responseList; // Return the list of responses
    }

    // Method to delete a review
    @Transactional
    public String deleteReview(DeleteReviewRequest reviewRequest) {

        // Fetch the review by its ID
        Review review = reviewRepository.findById(reviewRequest.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewRequest.getReviewId()));

        // Check if the user who is deleting the review is the one who wrote it
        if (!review.getUser().getUserId().equals(reviewRequest.getUserId())) {
            throw new RuntimeException("You can only delete your own reviews.");
        }

        // If the user is authorized, delete the review
        reviewRepository.delete(review);

        return "Review deleted successfully!";
    }
}

