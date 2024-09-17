package com.ecommerce.services;

import com.ecommerce.models.Product;
import com.ecommerce.models.ShoppingCart;
import com.ecommerce.models.User;
import com.ecommerce.repositories.ShoppingCartRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.requestDto.AddShoppingCartRequest;
import com.twilio.rest.chat.v2.service.user.UserBindingReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    // add cart
    @Transactional
    public String addCart(AddShoppingCartRequest request) {
        // Convert AddShoppingCartRequest to ShoppingCart entity
        ShoppingCart cart = new ShoppingCart();
        cart.setCartId(request.getCartId());
        cart.setCreationDate(request.getCreationDate());

        // Fetch user by userId
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));
        cart.setUser(user);

        // Check if cart already exists
        ShoppingCart existingCart = shoppingCartRepository.findById(cart.getCartId()).orElse(null);

        if (existingCart == null) {
            shoppingCartRepository.save(cart);
            return "Cart added successfully";
        } else {
            return "Cart already exists";
        }
    }


    @Transactional
    public String deleteCart(Long id){
        ShoppingCart cart = shoppingCartRepository.findById(id).orElse(null);
        if(cart!=null){
            shoppingCartRepository.delete(cart);
            return "Cart successfully deleted";
        }
        else{
            return "Cart with id:" + id +" not found";
        }

    }
}
