package com.ecommerce.services;

import com.ecommerce.models.*;
import com.ecommerce.repositories.*;
import com.ecommerce.requestDto.CancelOrderRequest;
import com.ecommerce.requestDto.OrderRequest;
import com.ecommerce.requestDto.UpdateOrderStatus;
import com.ecommerce.responseDto.GetOrderHistoryResponse;
import com.ecommerce.responseDto.GetOrderResponse;
import com.ecommerce.smsIntegration.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpService otpService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Place an order

    public String initiateOrderPlacement(Long userId) {
        // Generate and send a new OTP
        String newOtp = otpService.generateOtp();
        User user=userRepository.findById(userId).orElse(null);
        if(user!=null) {
            otpService.sendOtp(user.getPhoneNumber(), newOtp);
            // Store the OTP temporarily
            otpService.storeOtp(user.getPhoneNumber(), newOtp);
            return "New OTP has been sent to your phone. Please verify to complete the order.";

        }else{
            return "User not found";
        }

    }

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        // Fetch the shopping cart for the user
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser_UserId(orderRequest.getUserId());

        // If the shopping cart is not found, throw an exception
        if (!optionalShoppingCart.isPresent()) {
            throw new RuntimeException("Shopping cart not found for user with ID: " + orderRequest.getUserId());
        }

        ShoppingCart shoppingCart = optionalShoppingCart.get();
        User user = shoppingCart.getUser();

        // OTP validation before placing the order
        if (!otpService.validateOtp(user.getPhoneNumber(), orderRequest.getOtp())) {
            return "Invalid OTP. Please try again.";
        }

        // If the cart is empty, return an appropriate message
        if (shoppingCart.getCartItems().isEmpty()) {
            return "Shopping cart is empty, cannot place the order.";
        }

        // Create a new Order object before using it in the loop
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setDeliveryDate(null); // Set to null, to be updated when the order is dispatched
        order.setStatus("Processing");

        // Initialize total amount and order items list
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        // Iterate through the shopping cart items and check stock availability
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            Product product = cartItem.getProduct();
            int requestedQuantity = cartItem.getQuantity();
            int availableStock = product.getStockQuantity();

            // Check if the requested quantity is available in stock
            if (requestedQuantity > availableStock) {
                return "Sorry, the requested quantity for the product '" + product.getName() + "' is not available. Available stock: " + availableStock;
            }

            // If stock is available, decrease the stock quantity
            product.setStockQuantity(availableStock - requestedQuantity);
            productRepository.save(product); // Save the updated stock quantity

            // Create an order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQuantity);
            orderItem.setPrice(cartItem.getPrice());
            orderItems.add(orderItem);

            totalAmount += cartItem.getPrice() * requestedQuantity;
        }

        // Set order details
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Save the order to the repository
        orderRepository.save(order);

        // Clear the shopping cart after the order is placed
        cartItemRepository.deleteAll(shoppingCart.getCartItems());

        // Send order confirmation email
        sendOrderConfirmationEmail(user, order);

        return "Order successfully placed.";
    }


    @Transactional
    public GetOrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Convert Order to GetOrderResponse DTO
        return new GetOrderResponse(
                order.getOrderId(),
                order.getUser().getUsername(),  // Assuming you have a method getUsername() in User class
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getDeliveryDate()
        );
    }


    @Transactional
    public String updateOrderStatus(UpdateOrderStatus updateOrderStatus){
        Order order = orderRepository.findById(updateOrderStatus.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + updateOrderStatus.getOrderId()));

        order.setStatus(updateOrderStatus.getOrderStatus());
        orderRepository.save(order);
        return "order status successfully changed";
    }

    public void sendOrderConfirmationEmail(User user, Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Order Confirmation - Order ID: " + order.getOrderId());
        message.setText(String.format(
                "Dear %s,\n\nYour order has been successfully placed.\nOrder ID: %d\nTotal Amount: %.2f\nShipping Address: %s\n\nThank you for shopping with us!",
                user.getUsername(), order.getOrderId(), order.getTotalAmount(), order.getShippingAddress()
        ));
        message.setFrom("kaashifchishti611@gmail.com"); // Replace with your email
        javaMailSender.send(message);
    }

    //order history
    @Transactional
    public List<GetOrderHistoryResponse> getOrderHistory(Long userId) {
        // Fetch user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Fetch orders for the user
        List<Order> orders = orderRepository.findAllByUser(user);

        HashMap<String,Double> products=new HashMap<>();
        List<OrderItem> orderItemList=orders.getFirst().getOrderItems();

        for(OrderItem orderItem: orderItemList){

            Product product=orderItem.getProduct();
            String productName=product.getName();
            Double price=product.getPrice();
            products.put(productName,price);

        }

        // Convert orders to GetOrderHistoryResponse DTOs
        List<GetOrderHistoryResponse> responseList = new ArrayList<>();
        for (Order order : orders) {
            responseList.add(new GetOrderHistoryResponse(
                    order.getOrderId(),
                    order.getOrderDate(),
                    products,
                    order.getStatus(),
                    order.getTotalAmount(),
                    order.getShippingAddress(),
                    order.getDeliveryDate()
            ));
        }

        return responseList;
    }


    //cancel order
    @Transactional
    public String cancelOrder(CancelOrderRequest orderRequest) {
        // Fetch the user
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + orderRequest.getUserId()));

        // Fetch the order by ID
        Order order = orderRepository.findById(orderRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderRequest.getOrderId()));

        // Check if the order belongs to the user
        if (!order.getUser().equals(user)) {
            throw new RuntimeException("Order does not belong to the user");
        }

        // Optionally update the order status to "Canceled" instead of deleting
        order.setStatus("Canceled");
        orderRepository.save(order);

        // Return a success message
        return "Order successfully canceled with ID: " + orderRequest.getOrderId();
    }
}
