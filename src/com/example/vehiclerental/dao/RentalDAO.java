package com.example.vehiclerental.dao;

import com.example.vehiclerental.exception.DatabaseException;
import com.example.vehiclerental.model.Rental;
import com.example.vehiclerental.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    public void rentVehicle(int customerId, int vehicleId, Date rentalDate, Date returnDate) {
        String insertRental = "INSERT INTO rentals (customer_id, vehicle_id, rental_date, return_date, status) VALUES (?, ?, ?, ?, 'Rented')";
        String updateVehicle = "UPDATE vehicles SET status = 'Rented' WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(insertRental);
                 PreparedStatement ps2 = con.prepareStatement(updateVehicle)) {

                ps1.setInt(1, customerId);
                ps1.setInt(2, vehicleId);
                ps1.setDate(3, rentalDate);
                ps1.setDate(4, returnDate);
                ps1.executeUpdate();

                ps2.setInt(1, vehicleId);
                ps2.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw new DatabaseException("Rental transaction failed", e);
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error renting vehicle", e);
        }
    }

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
            throw new DatabaseException("Error checking rental existence", e);
        }

        return false;
    }

    public boolean isRentalAlreadyReturned(int rentalId) {
        String sql = "SELECT status FROM rentals WHERE rental_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rentalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Returned".equalsIgnoreCase(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking rental return status", e);
        }

        return false;
    }

    public void returnVehicle(int rentalId) {
        String getVehicleId = "SELECT vehicle_id FROM rentals WHERE rental_id = ?";
        String updateRental = "UPDATE rentals SET status = 'Returned' WHERE rental_id = ?";
        String updateVehicle = "UPDATE vehicles SET status = 'Available' WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int vehicleId = -1;

                try (PreparedStatement ps1 = con.prepareStatement(getVehicleId)) {
                    ps1.setInt(1, rentalId);
                    try (ResultSet rs = ps1.executeQuery()) {
                        if (rs.next()) {
                            vehicleId = rs.getInt("vehicle_id");
                        }
                    }
                }

                if (vehicleId == -1) {
                    throw new DatabaseException("Rental not found", null);
                }

                try (PreparedStatement ps2 = con.prepareStatement(updateRental);
                     PreparedStatement ps3 = con.prepareStatement(updateVehicle)) {

                    ps2.setInt(1, rentalId);
                    ps2.executeUpdate();

                    ps3.setInt(1, vehicleId);
                    ps3.executeUpdate();

                    con.commit();
                }

            } catch (Exception e) {
                con.rollback();
                throw new DatabaseException("Return transaction failed", e);
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error returning vehicle", e);
        }
    }

    public Date getExpectedReturnDate(int rentalId) {
        String sql = "SELECT return_date FROM rentals WHERE rental_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rentalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("return_date");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching expected return date", e);
        }

        return null;
    }

    public Date getRentalDate(int rentalId) {
        String sql = "SELECT rental_date FROM rentals WHERE rental_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rentalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("rental_date");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching rental date", e);
        }

        return null;
    }

    public List<Rental> getRentalsForCustomer(int customerId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals WHERE customer_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rentals.add(new Rental(
                            rs.getInt("rental_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("vehicle_id"),
                            rs.getDate("rental_date"),
                            rs.getDate("return_date"),
                            rs.getString("status")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching rentals for customer", e);
        }

        return rentals;
    }

    public boolean isVehicleCurrentlyRented(int vehicleId) {
        String sql = "SELECT COUNT(*) FROM rentals WHERE vehicle_id = ? AND status = 'Rented'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error checking vehicle rental status", e);
        }

        return false;
    }

    public List<String> getRentalsWithCustomers() {
        List<String> result = new ArrayList<>();

        String sql = "SELECT r.rental_id, c.name AS customer_name, v.model AS vehicle_model, " +
                     "r.rental_date, r.return_date, r.status " +
                     "FROM rentals r " +
                     "JOIN customers c ON r.customer_id = c.customer_id " +
                     "JOIN vehicles v ON r.vehicle_id = v.vehicle_id";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String data = "Rental ID: " + rs.getInt("rental_id") +
                        ", Customer: " + rs.getString("customer_name") +
                        ", Vehicle: " + rs.getString("vehicle_model") +
                        ", Rental Date: " + rs.getDate("rental_date") +
                        ", Return Date: " + rs.getDate("return_date") +
                        ", Status: " + rs.getString("status");

                result.add(data);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching rentals with customer details", e);
        }

        return result;
    }
}