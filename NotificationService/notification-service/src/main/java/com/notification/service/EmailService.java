package com.notification.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.notification.event.OrderEvent;

import jakarta.servlet.http.HttpSession;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    private HttpSession session;
    
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Send email using OrderEvent and dynamic email address
    public void sendEmail(OrderEvent orderEvent, String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("revshop@demomailtrap.com");
        message.setTo(recipientEmail);  // Use the dynamic email from OrderEvent
        
        message.setSubject("RevShop: Order Placed - ID: " + orderEvent.getOrderId());

        // Create a detailed message body
        String emailBody = String.format(
                "Dear Customer,\n\nYour order with ID: %d has been placed successfully!\n\n"
                + "Order Details:\n"
                + "Order ID: %d\n"
                + "Order Status: %s\n"
                + "Total Amount: %.2f\n"
                + "Shipping Address: %s\n"
                + "Billing Address: %s\n\n"
                + "Thank you for shopping with RevShop!\n",
                orderEvent.getOrderId(),
                orderEvent.getOrderId(),
                orderEvent.getStatus(),
                orderEvent.getTotalAmount(),
                orderEvent.getShippingAddress(),
                orderEvent.getBillingAddress()
        );
        
        message.setText(emailBody);

        // Send email
        mailSender.send(message);
    }

    // Kafka Listener to receive OrderEvent and trigger the email
    @KafkaListener(topics = "notificationTopic")
    public void getOrderEvent(OrderEvent orderEvent) {
        // Retrieve the user's email from the session
        String recipientEmail = (String) session.getAttribute("userEmail");
        
        if (recipientEmail != null) {
            sendEmail(orderEvent, recipientEmail);  // Use the dynamic email from the session
        } else {
            // Handle case where email is not available
            System.out.println("Error: User email not found in session");
        }
}
}
