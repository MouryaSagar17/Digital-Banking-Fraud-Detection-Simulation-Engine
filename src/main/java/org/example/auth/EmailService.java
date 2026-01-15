package org.example.auth;

import org.example.model.Transaction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mouryamourya289@gmail.com");
            message.setTo(to);
            message.setSubject("Your FraudGuard Login OTP");
            message.setText("Your One-Time Password for the Fraud Detection Dashboard is: " + otp);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
        }
    }

    public void sendBlockNotificationEmail(String to, Transaction transaction) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mouryamourya289@gmail.com");
            message.setTo(to);
            message.setSubject("Important Alert: Suspicious Transaction Detected on Your Account");

            // Format currency based on transaction
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", transaction.getCountry()));
            String formattedAmount = currencyFormatter.format(transaction.getTransactionAmount());

            String emailBody = String.format(
                "Dear Customer,\n\n" +
                "We detected a suspicious transaction on your account and have taken immediate action to protect you.\n\n" +
                "--- Transaction Details ---\n" +
                "Account Reference: %s\n" +
                "Transaction ID: %d\n" +
                "Transaction Amount: %s\n" +
                "Timestamp: %s\n" +
                "Status: Blocked due to suspected fraud\n\n" +
                "--- Reason for Alert ---\n" +
                "This transaction was flagged based on multiple security checks, including: %s.\n\n" +
                "--- What We’ve Done ---\n" +
                "- The transaction has been blocked.\n" +
                "- Your account (%s) has been temporarily suspended.\n\n" +
                "--- What You Should Do ---\n" +
                "- If this transaction was initiated by you, please contact our support team to verify and restore normal access.\n" +
                "- If you do not recognize this transaction, we recommend updating your password immediately and reviewing recent account activity.\n\n" +
                "Your security is our top priority.\n\n" +
                "Regards,\n" +
                "Security Team\n" +
                "Digital Banking Fraud Detection System",
                transaction.getCustomerId(),
                transaction.getId(),
                formattedAmount,
                transaction.getTxnTimestamp().toString(),
                transaction.getRiskRuleFlags(),
                transaction.getAccountId()
            );

            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("Block notification email sent to " + to);
        } catch (Exception e) {
            System.err.println("Error sending block notification email: " + e.getMessage());
        }
    }
}
