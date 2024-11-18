package com.example.demo;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.OtpRequest;
import com.example.demo.dto.ResetPasswordRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import com.example.demo.service.EmailService;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    public String registerUser(User user) {
    	Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            User existing = existingUser.get();
            if (existing.isEnabled()) {
                return "User already exists and is verified";
            } else {
                return "User already exists but not verified";
            }
        }
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);
        
        // Send verification OTP
        return sendOtp(user.getEmail());
    }

    public String loginUser(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            return "User not found";
        }
        
        User user = userOpt.get();
        if (!user.isEnabled()) {
            return "Please verify your email first";
        }
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return "Invalid password";
        }
        
        return "Login successful!";
    }

    public String sendOtp(OtpRequest otpRequest) {
        Optional<User> userOpt = userRepository.findByEmail(otpRequest.getEmail());
        if (userOpt.isEmpty()) {
            return "Email not found.";
        }
        
        User user = userOpt.get();
        String otp = String.format("%06d", new Random().nextInt(1000000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        // Send OTP via email
        try {
            emailService.sendOtpEmail(otpRequest.getEmail(), otp);
            return "OTP sent to " + otpRequest.getEmail();
        } catch (Exception e) {
            return "Failed to send OTP. Please try again.";
        }
    }
    public String sendOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Email not found.";
        }
        
        User user = userOpt.get();
        String otp = String.format("%06d", new Random().nextInt(1000000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        // Send OTP via email
        try {
            emailService.sendOtpEmail(email, otp);
            return "OTP sent to " + email;
        } catch (Exception e) {
            return "Failed to send OTP. Please try again.";
        }
    }
    
    public String verifyOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Email not found";
        }
        
        User user = userOpt.get();
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            return "No OTP request found";
        }
        
        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return "OTP has expired";
        }
        
        if (!user.getOtp().equals(otp)) {
            return "Invalid OTP";
        }
        
        user.setEnabled(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        
        return "OTP verified successfully!";
    }

    public String verifyOtp(OtpRequest otpRequest) {
        Optional<User> userOpt = userRepository.findByEmail(otpRequest.getEmail());
        if (userOpt.isEmpty()) {
            return "Email not found";
        }
        
        User user = userOpt.get();
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            return "No OTP request found";
        }
        
        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return "OTP has expired";
        }
        
        if (!user.getOtp().equals(otpRequest.getOtp())) {
            return "Invalid OTP";
        }
        
        user.setEnabled(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        
        return "OTP verified successfully!";
    }
    
    public String forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "User not found";
        }

        User user = userOpt.get();
        String otp = String.format("%06d", new Random().nextInt(1000000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        // Send OTP via email
        try {
            emailService.sendOtpEmail(email, otp);
            return "OTP sent to " + email;
        } catch (Exception e) {
            return "Failed to send OTP. Please try again.";
        }
    }
    
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Optional<User> userOpt = userRepository.findByEmail(resetPasswordRequest.getEmail());
        if (userOpt.isEmpty()) {
            return "User not found";
        }

        User user = userOpt.get();
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            return "No OTP request found";
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return "OTP has expired";
        }

        if (!user.getOtp().equals(resetPasswordRequest.getOtp())) {
            return "Invalid OTP";
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Password reset successful!";
    }
}