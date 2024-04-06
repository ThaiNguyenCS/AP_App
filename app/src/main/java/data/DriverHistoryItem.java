package data;

import java.util.List;

public class DriverHistoryItem {
    int ID;
    List<Integer> routeID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<Integer> getRouteID() {
        return routeID;
    }

    public void setRouteID(List<Integer> routeID) {
        this.routeID = routeID;
    }
}
