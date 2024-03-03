package data;


import java.util.Date;

public class Route {
    int _id;
    int driverID;
    int vehicleID;
    String departure;
    String destination;
    double distance;
    Date scheDepartureDate;
    Date scheArrivingDate;
    Date actualDepartureDate;
    Date actualArrivingDate;
    String status;
    double leftDistance; // used if this route is currently taken
    double cost;
    double revenue;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public Date getScheDepartureDate() {
        return scheDepartureDate;
    }

    public void setScheDepartureDate(Date scheDepartureDate) {
        this.scheDepartureDate = scheDepartureDate;
    }

    public Date getScheArrivingDate() {
        return scheArrivingDate;
    }

    public void setScheArrivingDate(Date scheArrivingDate) {
        this.scheArrivingDate = scheArrivingDate;
    }

    public Date getActualDepartureDate() {
        return actualDepartureDate;
    }

    public void setActualDepartureDate(Date actualDepartureDate) {
        this.actualDepartureDate = actualDepartureDate;
    }

    public Date getActualArrivingDate() {
        return actualArrivingDate;
    }

    public void setActualArrivingDate(Date actualArrivingDate) {
        this.actualArrivingDate = actualArrivingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
