package data;

import com.google.firebase.Timestamp;

public class MaintenanceReport {
    public static String MAINTENANCE_ID = "ID";
    public static String MAINTENANCE_VEHICLE_ID = "vehicleID";
    public static String MAINTENANCE_BEGIN = "beginDate";
    public static String MAINTENANCE_FINISH = "finishDate";
    public static String MAINTENANCE_COST = "cost";
    public static String MAINTENANCE_DESCRIPTION = "description";
    private int ID;
    private int vehicleID;
    private Timestamp beginDate;
    private Timestamp finishDate;
    private Double cost;
    private String description;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
