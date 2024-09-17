package com.ecommerce.controllers;

import com.ecommerce.requestDto.AddReviewRequest;
import com.ecommerce.requestDto.DeleteReviewRequest;
import com.ecommerce.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add-Review")
    public ResponseEntity<?> addReview(@RequestBody AddReviewRequest request) {
        try {
            String response = reviewService.addReview(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-All-Reviews-By-productId")
    public ResponseEntity<?> getAllReviewsByProductId(@RequestParam Long productId){

        try{
            return new ResponseEntity<>(reviewService.getReviewsByProductId(productId),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-All-Reviews-By-userId")
    public ResponseEntity<?> getAllReviewsByUserId(@RequestParam Long userId){

        try{
            return new ResponseEntity<>(reviewService.getReviewsByUserId(userId),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete-Review")
    public ResponseEntity<String> deleteReview(@RequestBody DeleteReviewRequest request) {
        try {
            String response = reviewService.deleteReview(request);
            return new ResponseEntity<>(response, HttpStatus.OK); // HTTP 200 for successful deletion
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // HTTP 400 if any error occurs
        }
    }
}
