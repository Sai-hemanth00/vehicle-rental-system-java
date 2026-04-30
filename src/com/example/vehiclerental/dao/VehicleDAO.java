package com.example.vehiclerental.dao;

import com.example.vehiclerental.exception.DatabaseException;
import com.example.vehiclerental.model.Vehicle;
import com.example.vehiclerental.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (model, type, registration_number, daily_rental_price, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vehicle.getModel());
            ps.setString(2, vehicle.getType());
            ps.setString(3, vehicle.getRegistrationNumber());
            ps.setDouble(4, vehicle.getDailyRentalPrice());
            ps.setString(5, vehicle.getStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding vehicle", e);
        }
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getString("registration_number"),
                        rs.getDouble("daily_rental_price"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching vehicles", e);
        }

        return vehicles;
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE status = 'Available'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getString("registration_number"),
                        rs.getDouble("daily_rental_price"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching available vehicles", e);
        }

        return vehicles;
    }

    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                            rs.getInt("vehicle_id"),
                            rs.getString("model"),
                            rs.getString("type"),
                            rs.getString("registration_number"),
                            rs.getDouble("daily_rental_price"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching vehicle by ID", e);
        }

        return null;
    }

    public void updateVehicleStatus(int vehicleId, String status) {
        String sql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, vehicleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating vehicle status", e);
        }
    }
}