package com.ecommerce.controllers;

import com.ecommerce.models.ShoppingCart;
import com.ecommerce.requestDto.AddShoppingCartRequest;
import com.ecommerce.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add-Cart")
    public ResponseEntity<?> addCart(@RequestBody AddShoppingCartRequest cart){
        try{
           String response= shoppingCartService.addCart(cart);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-Cart-By-CartId")
    public ResponseEntity<?> deleteCart(@RequestParam Long id){

        try{
            String response=shoppingCartService.deleteCart(id);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
