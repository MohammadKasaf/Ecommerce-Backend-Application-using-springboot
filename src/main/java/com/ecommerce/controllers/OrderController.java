package com.ecommerce.controllers;

import com.ecommerce.requestDto.CancelOrderRequest;
import com.ecommerce.requestDto.OrderRequest;
import com.ecommerce.requestDto.UpdateOrderStatus;
import com.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/initiate-Order-By-UserId")
    public ResponseEntity<String> initiateOrderPlacement(@RequestParam Long userId) {
        try {
            String responseMessage = orderService.initiateOrderPlacement(userId);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to initiate order placement: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/place-Order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            String responseMessage = orderService.placeOrder(orderRequest);
            if (responseMessage.contains("successfully")) {
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to place order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-Order-By-OrderId")
    public ResponseEntity<?> getOrder(@RequestParam Long orderId){

        try{
            return new ResponseEntity<>(orderService.getOrder(orderId),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-Order-Status")
    public ResponseEntity<?> getOrder(@RequestBody UpdateOrderStatus updateOrderStatus){

        try{
            return new ResponseEntity<>(orderService.updateOrderStatus(updateOrderStatus),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Order-History-By-UserId")
    public ResponseEntity<?> getOrderHistory(@RequestParam Long userId){

        try{
            return new ResponseEntity<>(orderService.getOrderHistory(userId),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cancel-Order")
    public ResponseEntity<?> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest){

        try{
            return new ResponseEntity<>(orderService.cancelOrder(cancelOrderRequest),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
