package data;

public class Vehicle {
    //TODO maintenance history
    int ID;
    int currentDriverID;
    int currentRouteID;
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

    public int getCurrentDriverID() {
        return currentDriverID;
    }

    public void setCurrentDriverID(int currentDriverID) {
        this.currentDriverID = currentDriverID;
    }

    public int getCurrentRouteID() {
        return currentRouteID;
    }

    public void setCurrentRouteID(int currentRouteID) {
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
