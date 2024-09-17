package com.ecommerce.controllers;

import com.ecommerce.models.CartItem;
import com.ecommerce.requestDto.AddCartItemRequest;
import com.ecommerce.responseDto.GetCartItemResponse;
import com.ecommerce.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add-CartItem")
    public ResponseEntity<?> addCartItem(@RequestBody AddCartItemRequest cartItem){

        try{
            String response=cartItemService.saveItem(cartItem);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // Get an item by ID
    @GetMapping("/get-Item-By-ItemId")
    public ResponseEntity<?> getItemById(@RequestParam Long id) {
        try {
            GetCartItemResponse response = cartItemService.getItem(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Get all items
    @GetMapping("/get-All-Items-By-CartId")
    public ResponseEntity<?> getAllItems(@RequestParam Long cartId) {
        try {
            List<GetCartItemResponse> items = cartItemService.getCartItemsByCartId(cartId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Delete an item by ID
    @DeleteMapping("/delete-Item-By-ItemId")
    public ResponseEntity<?> deleteItemById(@RequestParam Long id) {
        try {
            String result = cartItemService.deleteItem(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
