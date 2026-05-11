package com.srinivas.devassist.service;

import com.srinivas.devassist.model.OtpData;
import com.srinivas.devassist.model.User;
import com.srinivas.devassist.repo.UserRepo;
import com.srinivas.devassist.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Map<String, OtpData> loginOtpStore = new HashMap<>();
    private Map<String, OtpData> registerOtpStore = new HashMap<>();
    private Map<String, User> tempUserStore = new HashMap<>();

    // ================= LOGIN =================
    public String login(User user) {

        User existingUser = userRepo.findByUsername(user.getUsername());

        if (existingUser == null) {
            return "User not found";
        }

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Invalid password";
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        loginOtpStore.put(user.getUsername(),
                new OtpData(otp, LocalDateTime.now().plusMinutes(5)));

        emailService.sendOtp(user.getUsername(), otp);

        return "OTP sent to your email";
    }

    public String loginVerifyOtp(String username, String enteredOtp) {

        OtpData otpData = loginOtpStore.get(username);

        if (otpData == null) return "OTP not found!";

        if (otpData.getExpiryTime().isBefore(LocalDateTime.now())) {
            loginOtpStore.remove(username);
            return "OTP Expired!";
        }

        if (!otpData.getOtp().equals(enteredOtp)) return "Invalid OTP";

        loginOtpStore.remove(username);

        return jwtUtil.generateToken(username);
    }

    // ================= REGISTER =================
    public String register(User user) {

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password cannot be empty";
        }

        if (userRepo.findByUsername(user.getUsername()) != null) {
            return "Username already exists";
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        registerOtpStore.put(user.getUsername(),
                new OtpData(otp, LocalDateTime.now().plusMinutes(5)));

        // ✅ IMPORTANT: store full user
        tempUserStore.put(user.getUsername(), user);

        emailService.sendOtp(user.getUsername(), otp);

        return "OTP sent to your mail!";
    }

    public String registerVerifyOtp(String username, String enteredOtp) {

        OtpData otpData = registerOtpStore.get(username);

        if (otpData == null) return "OTP not found!";

        if (otpData.getExpiryTime().isBefore(LocalDateTime.now())) {
            registerOtpStore.remove(username);
            tempUserStore.remove(username);
            return "OTP Expired!";
        }

        if (!otpData.getOtp().equals(enteredOtp)) return "Invalid OTP";

        // ✅ GET USER
        User user = tempUserStore.get(username);

        if (user == null) return "User data missing!";

        // ✅ CRITICAL FIX (THIS SOLVES YOUR ERROR)
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password missing. Please register again.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        registerOtpStore.remove(username);
        tempUserStore.remove(username);

        return "Registered successfully";
    }

    // ================= RESEND =================
    public String resendLoginOtp(String username) {

        User existingUser = userRepo.findByUsername(username);

        if (existingUser == null) return "User not found";

        String otp = String.valueOf((int)(Math.random()*900000)+100000);

        loginOtpStore.put(username,
                new OtpData(otp, LocalDateTime.now().plusMinutes(5)));

        emailService.sendOtp(username, otp);

        return "OTP resent successfully";
    }

    public String resendRegisterOtp(String username) {

        if (!tempUserStore.containsKey(username)) {
            return "No registration request found";
        }

        String otp = String.valueOf((int)(Math.random()*900000)+100000);

        registerOtpStore.put(username,
                new OtpData(otp, LocalDateTime.now().plusMinutes(5)));

        emailService.sendOtp(username, otp);

        return "OTP resent successfully";
    }

    public String username(Authentication auth) {
        return "User: " + auth.getName();
    }
}