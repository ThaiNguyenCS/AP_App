package data;

public class Driver {
    //TODO history
    int _id;
    int currentVehicleID;
    int currentRouteID;
    String name;
    String citizenID;
    String phoneNumber;
    String license;
    String yearOfExperience;
    String address;
    String status;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(String yearOfExperience) {
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
