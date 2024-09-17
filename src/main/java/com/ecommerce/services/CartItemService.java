package com.ecommerce.services;

import com.ecommerce.models.CartItem;
import com.ecommerce.models.Product;
import com.ecommerce.models.ShoppingCart;
import com.ecommerce.repositories.CartItemRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.ShoppingCartRepository;
import com.ecommerce.requestDto.AddCartItemRequest;
import com.ecommerce.responseDto.GetCartItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    // save cart item
    @Transactional
    public String saveItem(AddCartItemRequest itemRequest) {
        // Validate the input item
        if (itemRequest == null || itemRequest.getCartId() == null || itemRequest.getProduct_id() == null) {
            return "Invalid item data";
        }

        // Fetch the cart and product from the database
        ShoppingCart cart = shoppingCartRepository.findById(itemRequest.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + itemRequest.getCartId()));

        Product product = productRepository.findById(itemRequest.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemRequest.getProduct_id()));

        // Fetch all items in the cart and check if the item exists
        List<CartItem> cartItems = cart.getCartItems();
        boolean itemExists = false;

        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(itemRequest.getProduct_id())) {
                // Item exists, update quantity and price
                item.setQuantity(item.getQuantity() + itemRequest.getQuantity());
                cartItemRepository.save(item);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            // Item does not exist, create and save a new item
            CartItem newItem = new CartItem();
            newItem.setShoppingCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(itemRequest.getQuantity());
            newItem.setPrice(product.getPrice());

            cartItemRepository.save(newItem);
        }

        return "Item processed successfully";
    }

    //get item
    public GetCartItemResponse getItem(Long id) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item with id " + id + " not found"));

        // Assuming CartItem has a getProduct() method that returns a Product object
        String productName = item.getProduct().getName();  // Adjust if needed

        return new GetCartItemResponse(
                item.getCartItemId(),
                productName,
                item.getQuantity(),
                item.getPrice()
        );
    }


    // Get all items
    public List<GetCartItemResponse> getCartItemsByCartId(Long cartId) {
        // Fetch all cart items for the given cart ID
        List<CartItem> cartItems = cartItemRepository.findByShoppingCart_CartId(cartId);

        // Create an empty list to hold GetCartItemResponse objects
        List<GetCartItemResponse> responseList = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            GetCartItemResponse response = new GetCartItemResponse();
            response.setCartItemId(cartItem.getCartItemId());
            response.setProductName(cartItem.getProduct().getName());
            response.setQuantity(cartItem.getQuantity());
            response.setPrice(cartItem.getProduct().getPrice());

            // Add the mapped response object to the list
            responseList.add(response);
        }

        return responseList; // Return the list of responses
    }



    //delete item
    @Transactional
    public String  deleteItem(Long id){
        CartItem item = cartItemRepository.findById(id).orElse(null);
        if (item!= null) {
            cartItemRepository.delete(item);
            return "Item successfully deleted";
        } else {
            return "Item is not found with id:" + id;
        }
    }

}
