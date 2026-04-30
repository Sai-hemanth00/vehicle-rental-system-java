package com.example.vehiclerental.main;

import com.example.vehiclerental.exception.DatabaseException;
import com.example.vehiclerental.model.Customer;
import com.example.vehiclerental.model.Vehicle;
import com.example.vehiclerental.service.CustomerService;
import com.example.vehiclerental.service.PaymentService;
import com.example.vehiclerental.service.RentalService;
import com.example.vehiclerental.service.VehicleService;

import java.sql.Date;
import java.util.Scanner;

public class VehicleRentalApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        VehicleService vehicleService = new VehicleService();
        CustomerService customerService = new CustomerService();
        RentalService rentalService = new RentalService();
        PaymentService paymentService = new PaymentService();

        while (true) {
            try {
                System.out.println("\n===== Vehicle Rental System =====");
                System.out.println("1. Add Vehicle");
                System.out.println("2. View All Vehicles");
                System.out.println("3. View Available Vehicles");
                System.out.println("4. Add Customer");
                System.out.println("5. Rent Vehicle");
                System.out.println("6. Return Vehicle");
                System.out.println("7. Process Payment");
                System.out.println("8. View Rentals with Customer Details");
                System.out.println("9. View All Payments");
                System.out.println("0. Exit");

                int choice = readInt(sc, "Enter your choice: ");

                switch (choice) {
                    case 1:
                        System.out.print("Enter vehicle model: ");
                        String model = sc.nextLine();

                        System.out.print("Enter vehicle type: ");
                        String type = sc.nextLine();

                        System.out.print("Enter registration number: ");
                        String regNo = sc.nextLine();

                        double price = readDouble(sc, "Enter daily rental price: ");

                        Vehicle vehicle = new Vehicle(model, type, regNo, price, "Available");
                        vehicleService.addVehicle(vehicle);
                        System.out.println("Vehicle added successfully.");
                        break;

                    case 2:
                        System.out.println("\n--- All Vehicles ---");
                        vehicleService.getAllVehicles().forEach(System.out::println);
                        break;

                    case 3:
                        System.out.println("\n--- Available Vehicles ---");
                        vehicleService.getAvailableVehicles().forEach(System.out::println);
                        break;

                    case 4:
                        System.out.print("Enter customer name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter phone: ");
                        String phone = sc.nextLine();

                        System.out.print("Enter license number: ");
                        String license = sc.nextLine();

                        Customer customer = new Customer(name, email, phone, license);
                        customerService.addCustomer(customer);
                        System.out.println("Customer added successfully.");
                        break;

                    case 5:
                        int customerId = readInt(sc, "Enter customer ID: ");
                        int vehicleId = readInt(sc, "Enter vehicle ID: ");

                        Date rentalDate = readDate(sc, "Enter rental date (yyyy-mm-dd): ");
                        Date returnDate = readDate(sc, "Enter return date (yyyy-mm-dd): ");

                        rentalService.rentVehicle(customerId, vehicleId, rentalDate, returnDate);
                        break;

                    case 6:
                        int rentalId = readInt(sc, "Enter rental ID to return: ");
                        rentalService.returnVehicle(rentalId);
                        break;

                    case 7:
                        int payRentalId = readInt(sc, "Enter rental ID: ");
                        double amount = readDouble(sc, "Enter payment amount: ");

                        System.out.print("Enter payment method: ");
                        String paymentMethod = sc.nextLine();

                        paymentService.processPayment(payRentalId, amount, paymentMethod);
                        break;

                    case 8:
                        System.out.println("\n--- Rentals with Customer Details ---");
                        rentalService.getRentalsWithCustomers().forEach(System.out::println);
                        break;

                    case 9:
                        System.out.println("\n--- All Payments ---");
                        paymentService.getAllPayments().forEach(System.out::println);
                        break;

                    case 0:
                        System.out.println("Exiting application...");
                        sc.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice! Please enter a number between 0 and 9.");
                }

            } catch (DatabaseException e) {
                System.out.println("Database Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }
    }

    private static int readInt(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(sc.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static double readDouble(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(sc.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid amount.");
            }
        }
    }

    private static Date readDate(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Date.valueOf(sc.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format! Please use yyyy-mm-dd.");
            }
        }
    }
}