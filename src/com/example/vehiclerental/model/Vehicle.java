package com.example.vehiclerental.model;

public class Vehicle {

    private int vehicleId;
    private String model;
    private String type;
    private String registrationNumber;
    private double dailyRentalPrice;
    private String status;

    public Vehicle() {
    }

    public Vehicle(int vehicleId, String model, String type, String registrationNumber, double dailyRentalPrice, String status) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.type = type;
        this.registrationNumber = registrationNumber;
        this.dailyRentalPrice = dailyRentalPrice;
        this.status = status;
    }

    public Vehicle(String model, String type, String registrationNumber, double dailyRentalPrice, String status) {
        this.model = model;
        this.type = type;
        this.registrationNumber = registrationNumber;
        this.dailyRentalPrice = dailyRentalPrice;
        this.status = status;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    public void setDailyRentalPrice(double dailyRentalPrice) {
        this.dailyRentalPrice = dailyRentalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", dailyRentalPrice=" + dailyRentalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}