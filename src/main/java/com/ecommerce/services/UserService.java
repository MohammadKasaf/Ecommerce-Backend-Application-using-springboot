package com.ecommerce.services;

import com.ecommerce.repositories.UserRepository;
import com.ecommerce.requestDto.AddUserRequest;
import com.ecommerce.requestDto.ResetPasswordRequest;
import com.ecommerce.requestDto.UpdateUserRequest;
import com.ecommerce.responseDto.GetUserResponse;
import com.ecommerce.smsIntegration.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import com.ecommerce.models.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OtpService otpService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user=userRepository.findByUsername(username);
        if(user.isPresent()){

            var userObj=user.get();
            return org.springframework.security.core.userdetails.User.builder().username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(userObj.getRole())
                    .build();
        }
        else{

            throw new UsernameNotFoundException(username);
        }
    }

    // Temporary storage to hold user data until OTP is verified
    private ConcurrentHashMap<String, User> temporaryUserStore = new ConcurrentHashMap<>();


    // Method to send OTP
    public String registerUser(AddUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        // Generate and send OTP
        String otp = otpService.generateOtp();
        otpService.sendOtp(user.getPhoneNumber(), otp);

        // Temporarily store user data until OTP is verified
        temporaryUserStore.put(user.getPhoneNumber(), user);

        return "OTP has been sent to " + user.getPhoneNumber() + ". Please verify to complete registration.";
    }

    // Method to save user after OTP is successfully verified
    @Transactional
    public String confirmUserRegistration(String phoneNumber) {

        User user = temporaryUserStore.get(phoneNumber);
        if (user != null) {
            userRepository.save(user); // Save user to the database
            temporaryUserStore.remove(phoneNumber); // Remove from temporary storage

            // Send registration success email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Registration Successful");
            message.setText("Your registration is successful. Your username is: " + user.getUsername());
            message.setFrom("no-reply@yourdomain.com"); // Use a no-reply email or your registered email
            javaMailSender.send(message);

            return "User registered successfully!";
        } else {
            return "No user data found for phone number: " + phoneNumber;
        }
    }

    // Method to fetch user and return response DTO
    public GetUserResponse getUserResponse(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

        // Map User to GetUserResponse
        return new GetUserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getAddress(),
                user.getPhoneNumber()
        );
    }

    // Method to fetch all users and return a list of response DTOs
    public List<GetUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<GetUserResponse> responses = new ArrayList<>();

        for (User user : users) {
            GetUserResponse response = new GetUserResponse(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.getAddress(),
                    user.getPhoneNumber()
            );
            responses.add(response);
        }

        return responses;
    }

    //update user
    @Transactional
    public String updateUser(UpdateUserRequest userRequest){

        User user=userRepository.findById(userRequest.getUserId()).orElse(null);
        if(user==null){
            return "user with id:" +userRequest.getUserId() +"not found";
        }

        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        userRepository.save(user);
        return "User updated successfully";
    }

    //delete user
    @Transactional
    public String deleteUser(Long id){

        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    //reset password
    @Transactional
    public String resetPassword(ResetPasswordRequest passwordRequest){

        User user=userRepository.findById(passwordRequest.getUserId()).orElse(null);
        if(user==null){
            return "user is not found with id: "+passwordRequest.getUserId();
        }
        user.setPassword(passwordRequest.getNewPassword());
        userRepository.save(user);
        return "password successfully changed";
    }


}
