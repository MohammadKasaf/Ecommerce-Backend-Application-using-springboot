package com.ecommerce.smsIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private TwilioConfiguration twilioConfiguration;

    private ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void storeOtp(String phoneNumber, String otp) {
        otpStore.put(phoneNumber, otp); // Store OTP temporarily
    }

    public void sendOtp(String phoneNumber, String otp) {
        String message = "Your OTP code is: " + otp;
        twilioConfiguration.sendSms(phoneNumber, message);
        otpStore.put(phoneNumber, otp);
    }

    public boolean validateOtp(String phoneNumber, String otp) {
        String storedOtp = otpStore.get(phoneNumber);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void removeOtp(String phoneNumber) {
        otpStore.remove(phoneNumber);
    }
}
