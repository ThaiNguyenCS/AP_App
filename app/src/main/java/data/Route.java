package data;


import com.google.firebase.Timestamp;

import java.util.Date;

public class Route {
    int ID;
    int driverID;
    int vehicleID;
    String departure;
    String destination;
    double distance;
    Timestamp scheDepartureDate;
    Timestamp scheArrivingDate;
    Timestamp actualDepartureDate;
    Timestamp actualArrivingDate;
    int status;
    double leftDistance; // used if this route is currently taken
    double cost;
    double revenue;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Timestamp getScheDepartureDate() {
        return scheDepartureDate;
    }

    public void setScheDepartureDate(Timestamp scheDepartureDate) {
        this.scheDepartureDate = scheDepartureDate;
    }

    public Timestamp getScheArrivingDate() {
        return scheArrivingDate;
    }

    public void setScheArrivingDate(Timestamp scheArrivingDate) {
        this.scheArrivingDate = scheArrivingDate;
    }

    public Timestamp getActualDepartureDate() {
        return actualDepartureDate;
    }

    public void setActualDepartureDate(Timestamp actualDepartureDate) {
        this.actualDepartureDate = actualDepartureDate;
    }

    public Timestamp getActualArrivingDate() {
        return actualArrivingDate;
    }

    public void setActualArrivingDate(Timestamp actualArrivingDate) {
        this.actualArrivingDate = actualArrivingDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLeftDistance() {
        return leftDistance;
    }

    public void setLeftDistance(double leftDistance) {
        this.leftDistance = leftDistance;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
