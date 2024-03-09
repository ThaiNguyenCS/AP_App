package data;

import com.google.firebase.firestore.PropertyName;

public class Driver {

    //TODO history
    public static String DRIVER_ID = "ID";
    public static String DRIVER_NAME = "name";
    public static String DRIVER_PHONE = "phoneNumber";
    public static String DRIVER_CITIZENID = "citizenId";
    public static String DRIVER_YOE = "yearOfExperience";
    public static String DRIVER_ADDRESS = "address";
    public static String DRIVER_STATUS = "status";
    public static String DRIVER_LICENSE = "license";
    public static String DRIVER_ROUTE_ID = "currentRouteID";
    public static String DRIVER_VEHICLE_ID = "currentVehicleID";

    int ID;
    Integer currentVehicleID;
    Integer currentRouteID;
    String name;
    @PropertyName("citizenId")
    String citizenID;
    String phoneNumber;
    String license;
    long yearOfExperience;
    String address;
    long status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Integer getCurrentVehicleID() {
        return currentVehicleID;
    }

    public void setCurrentVehicleID(Integer currentVehicleID) {
        this.currentVehicleID = currentVehicleID;
    }

    public Integer getCurrentRouteID() {
        return currentRouteID;
    }

    public void setCurrentRouteID(Integer currentRouteID) {
        this.currentRouteID = currentRouteID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public long getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(long yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}
