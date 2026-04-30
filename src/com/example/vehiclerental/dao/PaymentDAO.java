package com.example.vehiclerental.dao;

import com.example.vehiclerental.exception.DatabaseException;
import com.example.vehiclerental.model.Payment;
import com.example.vehiclerental.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public boolean isRentalExists(int rentalId) {
        String sql = "SELECT COUNT(*) FROM rentals WHERE rental_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rentalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error checking rental ID", e);
        }

        return false;
    }

    public boolean isPaymentAlreadyDone(int rentalId) {
        String sql = "SELECT COUNT(*) FROM payments WHERE rental_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rentalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error checking existing payment", e);
        }

        return false;
    }

    public boolean processPayment(int rentalId, double amount, String paymentMethod) {
        if (!isRentalExists(rentalId)) {
            return false;
        }

        if (isPaymentAlreadyDone(rentalId)) {
            throw new DatabaseException("Payment already exists for this rental.", null);
        }

        String sql = "INSERT INTO payments (rental_id, amount, payment_date, payment_method) VALUES (?, ?, CURDATE(), ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rentalId);
            ps.setDouble(2, amount);
            ps.setString(3, paymentMethod);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw new DatabaseException("Error processing payment", e);
        }
    }

    public List<Payment> getPaymentDetails() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("rental_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_method")
                ));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching payments", e);
        }

        return payments;
    }
}