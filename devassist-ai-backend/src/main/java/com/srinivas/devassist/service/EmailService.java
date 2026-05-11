package com.srinivas.devassist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtp(String toEmail,String otp){

        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("DevAssist OTP Verification");
        message.setText(
                "Hello,\n\nYour OTP is: " + otp +
                        "\nValid for 5 minutes.\n\nDo not share this OTP."
        );

        javaMailSender.send(message);

    }
}
