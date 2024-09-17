package com.ecommerce.smsIntegration;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {

    @Value("${spring.twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${spring.twilio.auth-token}")
    private String AUTH_TOKEN;

    @Value("${spring.twilio.phone-number}")
    private String FROM_PHONE_NUMBER;

    public void sendSms(String to, String messageBody) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(FROM_PHONE_NUMBER),
                            messageBody)
                    .create();
            System.out.println("SMS sent successfully: " + message.getSid());
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception or handle it accordingly
        }
    }

}
