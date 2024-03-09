package data;

public class Vehicle {
    //TODO maintenance history
    public static String VEHICLE_TYPE = "type";
    public static String VEHICLE_PLATE = "numberPlate";
    public static String VEHICLE_HEIGHT = "height";
    public static String VEHICLE_WIDTH = "width";
    public static String VEHICLE_LENGTH = "length";
    public static String VEHICLE_STATUS = "status";
    public static String VEHICLE_LOAD = "maximumLoad";
    public static String VEHICLE_FUEL = "typeOfFuel";
    public static String VEHICLE_ROUTE_ID = "currentRouteID";
    public static String VEHICLE_DRIVER_ID = "currentDriverID";

    int ID;
    Integer currentDriverID;
    Integer currentRouteID;
    int status;
    String type;
    String numberPlate; // bien so xe
    double height;
    double width;
    double length;
    double maximumLoad;
    String typeOfFuel;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public Integer getCurrentRouteID() {
        return currentRouteID;
    }

    public void setCurrentRouteID(Integer currentRouteID) {
        this.currentRouteID = currentRouteID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getMaximumLoad() {
        return maximumLoad;
    }

    public void setMaximumLoad(double maximumLoad) {
        this.maximumLoad = maximumLoad;
    }

    public String getTypeOfFuel() {
        return typeOfFuel;
    }

    public void setTypeOfFuel(String typeOfFuel) {
        this.typeOfFuel = typeOfFuel;
    }
}
