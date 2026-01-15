package org.example.auth;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private static final long OTP_VALID_DURATION = 5; // 5 minutes
    private final Map<String, OtpDetails> otpCache = new HashMap<>();

    public String generateOtp(String email) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));
        
        OtpDetails details = new OtpDetails(otp, LocalDateTime.now());
        otpCache.put(email, details);
        
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpDetails details = otpCache.get(email);
        if (details == null) {
            return false; // No OTP was generated for this email
        }

        // Check if OTP is expired
        if (details.getTimestamp().plusMinutes(OTP_VALID_DURATION).isBefore(LocalDateTime.now())) {
            otpCache.remove(email); // Clean up expired OTP
            return false;
        }

        if (details.getOtp().equals(otp)) {
            otpCache.remove(email); // OTP is used, remove it
            return true;
        }

        return false;
    }

    private static class OtpDetails {
        private final String otp;
        private final LocalDateTime timestamp;

        public OtpDetails(String otp, LocalDateTime timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
