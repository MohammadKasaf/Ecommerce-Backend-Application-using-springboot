package com.ecommerce.controllers;

import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.requestDto.AddUserRequest;
import com.ecommerce.requestDto.ResetPasswordRequest;
import com.ecommerce.requestDto.UpdateUserRequest;
import com.ecommerce.responseDto.GetUserResponse;
import com.ecommerce.services.UserService;
import com.ecommerce.smsIntegration.OtpService;
import com.ecommerce.webtoken.JwtService;
import com.ecommerce.webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;


    @Autowired
    private UserRepository userRepository;

    // Register a new user
    @PostMapping("/register-User")
    public ResponseEntity<?> registerUser(@RequestBody AddUserRequest user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Authenticate user and get token
    @PostMapping("/authenticate-User")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginForm.username(), loginForm.password()
            ));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userService.loadUserByUsername(loginForm.username()));
                return ResponseEntity.ok(token);
            } else {
                throw new UsernameNotFoundException("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Get user by ID
    @GetMapping("/get-User-By-UserId")
    public ResponseEntity<?> getUser(@RequestParam Long id) {
        try {
            GetUserResponse response = userService.getUserResponse(id);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all users
    @GetMapping("/get-All-Users")
    public ResponseEntity<?> getAllUsers() {
        List<GetUserResponse> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    // Update user
    @PutMapping("/update-User")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest userRequest) {

        User user = userRepository.findById(userRequest.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID: " + userRequest.getUserId() + " not found");
        }
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        String result = userService.updateUser(userRequest);
        if (result.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }

    // Delete user
    @DeleteMapping("/delete-User-By-UserId")
    public ResponseEntity<String> deleteUser(@RequestParam Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID: " + id + " not found");
        }
    }

    @PostMapping("/verify-Otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(phoneNumber, otp);

        if (isValid) {
            otpService.removeOtp(phoneNumber); // Remove OTP after successful verification

            // Save the user to the database now that OTP is verified
            String saveUserMessage = userService.confirmUserRegistration(phoneNumber);

            return ResponseEntity.ok("OTP verified successfully. " + saveUserMessage);
        } else {
            return ResponseEntity.status(400).body("Invalid OTP");
        }
    }

    @PostMapping("/password-Reset")
    public ResponseEntity<?> passwordReset(@RequestBody ResetPasswordRequest passwordRequest) {

        try {
            passwordRequest.setNewPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            String response = userService.resetPassword(passwordRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



}

