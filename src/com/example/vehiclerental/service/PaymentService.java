package com.example.vehiclerental.service;

import com.example.vehiclerental.dao.PaymentDAO;
import com.example.vehiclerental.exception.DatabaseException;
import com.example.vehiclerental.model.Payment;

import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    public void processPayment(int rentalId, double amount, String paymentMethod) {
        try {
            boolean isSuccess = paymentDAO.processPayment(rentalId, amount, paymentMethod);

            if (isSuccess) {
                System.out.println("Payment processed successfully.");
            } else {
                System.out.println("Invalid rental ID. Payment cannot be processed.");
            }
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getPaymentDetails();
    }
}