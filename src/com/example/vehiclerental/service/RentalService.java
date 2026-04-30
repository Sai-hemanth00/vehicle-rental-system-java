package com.example.vehiclerental.service;

import com.example.vehiclerental.dao.CustomerDAO;
import com.example.vehiclerental.dao.RentalDAO;
import com.example.vehiclerental.dao.VehicleDAO;
import com.example.vehiclerental.model.Vehicle;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {

    private final RentalDAO rentalDAO = new RentalDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    private static final double LATE_FEE_PER_DAY = 500.0;

    public void rentVehicle(int customerId, int vehicleId, Date rentalDate, Date returnDate) {

        if (!customerDAO.isCustomerExists(customerId)) {
            System.out.println("Invalid customer ID. Customer does not exist.");
            return;
        }

        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

        if (vehicle == null) {
            System.out.println("Invalid vehicle ID. Vehicle not found.");
            return;
        }

        if (!"Available".equalsIgnoreCase(vehicle.getStatus()) || rentalDAO.isVehicleCurrentlyRented(vehicleId)) {
            System.out.println("Vehicle is not available for rent.");
            return;
        }

        if (returnDate.before(rentalDate)) {
            System.out.println("Return date cannot be before rental date.");
            return;
        }

        rentalDAO.rentVehicle(customerId, vehicleId, rentalDate, returnDate);
        System.out.println("Vehicle rented successfully.");
    }

    public void returnVehicle(int rentalId) {

        if (!rentalDAO.isRentalExists(rentalId)) {
            System.out.println("Invalid rental ID. Rental not found.");
            return;
        }

        if (rentalDAO.isRentalAlreadyReturned(rentalId)) {
            System.out.println("This vehicle has already been returned.");
            return;
        }

        Date expectedReturnDate = rentalDAO.getExpectedReturnDate(rentalId);
        LocalDate today = LocalDate.now();

        if (expectedReturnDate != null && today.isAfter(expectedReturnDate.toLocalDate())) {
            long lateDays = ChronoUnit.DAYS.between(expectedReturnDate.toLocalDate(), today);
            double lateFee = lateDays * LATE_FEE_PER_DAY;

            System.out.println("Vehicle is returned late by " + lateDays + " day(s).");
            System.out.println("Late fee to be collected: Rs. " + lateFee);
        } else {
            System.out.println("Vehicle returned on time. No late fee.");
        }

        rentalDAO.returnVehicle(rentalId);
        System.out.println("Vehicle returned successfully.");
    }

    public List<String> getRentalsWithCustomers() {
        return rentalDAO.getRentalsWithCustomers();
    }
}