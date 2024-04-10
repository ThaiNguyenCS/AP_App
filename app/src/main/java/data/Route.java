package data;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Route {
    public static String ROUTE_ID = "ID";
    public static String ROUTE_VEHICLE_ID = "currentVehicleID";
    public static String ROUTE_DRIVER_ID = "currentDriverID";
    public static String ROUTE_DEPARTURE = "departure";
    public static String ROUTE_DESTINATION = "destination";
    public static String ROUTE_DISTANCE = "distance";
    public static String ROUTE_SCHE_DEPART = "scheDepartureDate";
    public static String ROUTE_SCHE_ARRIVE = "scheArrivingDate";
    public static String ROUTE_ACTUAL_ARRIVE = "actualArrivingDate";
    public static String ROUTE_ACTUAL_DEPART = "actualDepartureDate";
    public static String ROUTE_STATUS = "status";
    public static String ROUTE_LEFT_DIST = "leftDistance";
    public static String ROUTE_COST = "cost";
    public static String ROUTE_REVENUE = "revenue";
    int ID;
    Integer currentDriverID;
    Integer currentVehicleID;
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

    public Integer getCurrentDriverID() {
        return currentDriverID;
    }

    public void setCurrentDriverID(Integer currentDriverID) {
        this.currentDriverID = currentDriverID;
    }

    public Integer getCurrentVehicleID() {
        return currentVehicleID;
    }

    public void setCurrentVehicleID(Integer currentVehicleID) {
        this.currentVehicleID = currentVehicleID;
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
