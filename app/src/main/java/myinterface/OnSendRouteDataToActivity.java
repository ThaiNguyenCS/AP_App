package myinterface;

import com.google.firebase.Timestamp;

public interface OnSendRouteDataToActivity {
    void sendRouteDataToActivity(String departure, String destination, Timestamp depart, Timestamp arrive, double cost, double revenue);
}
