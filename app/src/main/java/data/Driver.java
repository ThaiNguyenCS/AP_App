package data;

import com.google.firebase.firestore.PropertyName;

public class Driver {
    //TODO history
    int ID;
    int currentVehicleID;
    int currentRouteID;
    String name;
    @PropertyName("citizenId")
    String citizenID;
    String phoneNumber;
    String license;
    long yearOfExperience;
    String address;
    String status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCurrentVehicleID() {
        return currentVehicleID;
    }

    public void setCurrentVehicleID(int currentVehicleID) {
        this.currentVehicleID = currentVehicleID;
    }

    public int getCurrentRouteID() {
        return currentRouteID;
    }

    public void setCurrentRouteID(int currentRouteID) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
